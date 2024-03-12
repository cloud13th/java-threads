package org.cloud13th.threads.normal.kmeans.parallel;

import org.cloud13th.threads.normal.kmeans.common.document.DocumentCluster;

import java.util.concurrent.RecursiveAction;

public class UpdateTask extends RecursiveAction {

    private static final long serialVersionUID = 1005287726225369344L;

    private final DocumentCluster[] clusters;
    // 任务要处理的簇数
    private final int start;
    private final int end;
    // 一个任务可处理的最大簇数
    private final int maxSize;

    public UpdateTask(DocumentCluster[] clusters, int start, int end, int maxSize) {
        this.clusters = clusters;
        this.start = start;
        this.end = end;
        this.maxSize = maxSize;
    }

    // 重新计算每个簇的质心作为该簇中所有文档的平均值
    @Override
    protected void compute() {
        // 使用一个任务要处理的簇数作为指标来控制是否要对任务进行分割
        // 从一个需要处理所有簇的任务开始，对其进行分割，直到任务要处理的簇比预定规模小为止
        if (end - start <= maxSize) {
            for (int i = start; i < end; i++) {
                DocumentCluster cluster = clusters[i];
                cluster.calculateCentroid();
            }
        } else {
            int mid = (start + end) / 2;
            UpdateTask task1 = new UpdateTask(clusters, start, mid, maxSize);
            UpdateTask task2 = new UpdateTask(clusters, mid, end, maxSize);
            invokeAll(task1, task2);
        }
    }

}
