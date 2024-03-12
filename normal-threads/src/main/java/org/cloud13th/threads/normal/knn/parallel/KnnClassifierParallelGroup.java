package org.cloud13th.threads.normal.knn.parallel;

import org.cloud13th.threads.normal.knn.common.data.Distance;
import org.cloud13th.threads.normal.knn.common.data.Sample;
import org.cloud13th.threads.normal.knn.common.distances.EuclideanDistanceCalculator;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class KnnClassifierParallelGroup {

    private final List<? extends Sample> dataSet;
    private final int k;
    private final int numThreads;
    private final ThreadPoolExecutor executor;
    private final boolean parallelSort;

    public KnnClassifierParallelGroup(List<? extends Sample> dataSet, int k, boolean parallelSort) {
        this.dataSet = dataSet;
        this.k = k;
        this.numThreads = Runtime.getRuntime().availableProcessors();
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(this.numThreads);
        this.parallelSort = parallelSort;
    }

    public String classify(Sample example) throws InterruptedException {
        Distance[] distances = new Distance[dataSet.size()];
        CountDownLatch endController = new CountDownLatch(numThreads);

        int length = dataSet.size() / numThreads;
        int startIndex = 0;
        int endIndex = length;

        for (int i = 0; i < numThreads; i++) {
            GroupDistanceTask task = new GroupDistanceTask(distances, startIndex, endIndex, dataSet, example, endController);
            startIndex = endIndex;
            if (i < numThreads - 2) {
                endIndex = endIndex + length;
            } else {
                endIndex = dataSet.size();
            }
            executor.execute(task);
        }
        endController.await();

        if (parallelSort) {
            Arrays.parallelSort(distances);
        } else {
            Arrays.sort(distances);
        }

        Map<String, Integer> results = new HashMap<>();
        for (int i = 0; i < k; i++) {
            Sample localExample = dataSet.get(distances[i].getIndex());
            String tag = localExample.getTag();
            results.merge(tag, 1, Integer::sum);
        }
        return Collections.max(results.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    public void destroy() {
        executor.shutdown();
    }
}

class GroupDistanceTask implements Runnable {

    private final Distance[] distances;
    private final int startIndex;
    private final int endIndex;
    private final Sample example;
    private final List<? extends Sample> dataSet;
    private final CountDownLatch endController;

    public GroupDistanceTask(Distance[] distances, int startIndex, int endIndex, List<? extends Sample> dataSet,
                             Sample example, CountDownLatch endController) {
        this.distances = distances;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.example = example;
        this.dataSet = dataSet;
        this.endController = endController;
    }

    @Override
    public void run() {
        for (int index = startIndex; index < endIndex; index++) {
            Sample localExample = dataSet.get(index);
            distances[index] = new Distance();
            distances[index].setIndex(index);
            distances[index].setDistance(EuclideanDistanceCalculator.calculate(localExample, example));
        }
        endController.countDown();
    }

}
