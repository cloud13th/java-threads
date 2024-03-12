package org.cloud13th.threads.normal.knn.parallel;

import org.cloud13th.threads.normal.knn.common.data.Distance;
import org.cloud13th.threads.normal.knn.common.data.Sample;
import org.cloud13th.threads.normal.knn.common.distances.EuclideanDistanceCalculator;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class KnnClassifierParallelIndividual {

    private final List<? extends Sample> dataSet;
    private final int k;
    private final ThreadPoolExecutor executor;
    private final int numThreads;
    private final boolean parallelSort;

    public KnnClassifierParallelIndividual(List<? extends Sample> dataSet, int k, int factor, boolean parallelSort) {
        this.dataSet = dataSet;
        this.k = k;
        numThreads = factor * (Runtime.getRuntime().availableProcessors());
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);
        this.parallelSort = parallelSort;
    }

    // 细粒度并发版本
    public String classify(Sample example) throws InterruptedException {
        Distance[] distances = new Distance[dataSet.size()];
        // CountDownLatch 类允许一个线程一直等待，直到其他线程到达其代码的某一确定点
        // 该类需要使用等待线程数 进行初始化
        CountDownLatch endController = new CountDownLatch(dataSet.size());

        int index = 0;
        // 细粒度并发版本
        for (Sample sample : dataSet) {
            // 为每个需要计算的距离创建一个任务，并且将其发送给执行器。
            IndividualDistanceTask task = new IndividualDistanceTask(distances, index, sample, example, endController);
            executor.execute(task);
            index++;
        }
        // 主线程等待这些任 务执行结束
        // await() 方法挂起调用它的线程(这里指主线程)，直到计数器达到 0 为止
        endController.await();

        if (parallelSort) {
            Arrays.parallelSort(distances);
        } else {
            Arrays.sort(distances);
        }

        Map<String, Integer> results = new HashMap<>();
        for (int i = 0; i < k; i++) {
            Sample sample = dataSet.get(distances[i].getIndex());
            String tag = sample.getTag();
            results.merge(tag, 1, Integer::sum);
        }
        return Collections.max(results.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    // 如果不调用该方法，应用程序就不会结束，因为执行器所创建的线程仍然存在，并且在等待处理新任务
    // 在此之前提交的任务已执行完毕，而新提交的任务会被拒绝
    // 该方法并不会等待执行器完成，它会立即返回
    public void destroy() {
        executor.shutdown();
    }

}

class IndividualDistanceTask implements Runnable {

    // 尽管所有任务共享 distances 数组，但是这里并不需要采用任何同步机制，因为每个任务只会修改该数组不同位置的数据
    private final Distance[] distances;
    private final int index;
    private final Sample localExample;
    private final Sample example;
    private final CountDownLatch endController;

    public IndividualDistanceTask(Distance[] distances, int index, Sample localExample, Sample example, CountDownLatch endController) {
        this.distances = distances;
        this.index = index;
        this.localExample = localExample;
        this.example = example;
        this.endController = endController;
    }

    @Override
    public void run() {
        // 将输入test与训练数据集中某个data之间的距离作为一项并发任务计算
        distances[index] = new Distance();
        distances[index].setIndex(index);
        distances[index].setDistance(EuclideanDistanceCalculator.calculate(localExample, example));
        endController.countDown();
    }

}
