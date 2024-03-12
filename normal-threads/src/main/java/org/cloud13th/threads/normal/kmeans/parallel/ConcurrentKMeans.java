package org.cloud13th.threads.normal.kmeans.parallel;

import org.cloud13th.threads.normal.kmeans.common.document.Document;
import org.cloud13th.threads.normal.kmeans.common.document.DocumentCluster;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentKMeans {

    private ConcurrentKMeans() {
    }

    // maxSize:在不将 Fork/Join 任务分割成其他任务的前提下，该任务所要处理的最大项数
    public static DocumentCluster[] calculate(Document[] documents, int clusterNum, int vocSize, int seed, int maxSize) {
        // 基于文档创建由 numberClusters 参数指定数目的簇数组
        DocumentCluster[] clusters = new DocumentCluster[clusterNum];
        Random random = new Random(seed);
        for (int i = 0; i < clusterNum; i++) {
            clusters[i] = new DocumentCluster(vocSize, new ConcurrentLinkedQueue<>());
            // 初始化簇
            clusters[i].initialize(random);
        }
        boolean change = true;
        ForkJoinPool pool = new ForkJoinPool();
        // 重复指派阶段和更新阶段，直到所有文档所属的簇都不再改变为止
        while (change) {
            change = assignment(clusters, documents, maxSize, pool);
            update(clusters, maxSize, pool);

            System.out.println("**********************");
            System.out.println("Parallelism: " + pool.getParallelism());
            System.out.println("Pool Size: " + pool.getPoolSize());
            System.out.println("Active Thread Count: " + pool.getActiveThreadCount());
            System.out.println("Running Thread Count: " + pool.getRunningThreadCount());
            System.out.println("Queued Submission: " + pool.getQueuedSubmissionCount());
            System.out.println("Queued Tasks: " + pool.getQueuedTaskCount());
            System.out.println("Queued Submissions: " + pool.hasQueuedSubmissions());
            System.out.println("Steal Count: " + pool.getStealCount());
            System.out.println("Terminated : " + pool.isTerminated());
            System.out.println("**********************\n");
        }
        pool.shutdown();
        return clusters;
    }

    private static boolean assignment(DocumentCluster[] clusters, Document[] documents, int maxSize, ForkJoinPool pool) {
        // 删除所有簇的关联文档列表
        for (DocumentCluster cluster : clusters) {
            cluster.clearClusters();
        }
        // 初始化
        AtomicInteger numChanges = new AtomicInteger(0); // 用于存放已指派簇发生变化的文档数
        AssignmentTask task = new AssignmentTask(clusters, documents, 0, documents.length, numChanges, maxSize); // 用于启动处理过程
        // 异步方式执行池中的任务
        pool.execute(task);
        task.join();
        // 检查已改变指派簇的文档数
        return numChanges.get() > 0;
    }

    private static void update(DocumentCluster[] clusters, int maxSize, ForkJoinPool pool) {
        UpdateTask task = new UpdateTask(clusters, 0, clusters.length, maxSize);
        pool.execute(task);
        task.join();
    }

}
