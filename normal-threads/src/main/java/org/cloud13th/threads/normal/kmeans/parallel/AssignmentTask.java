package org.cloud13th.threads.normal.kmeans.parallel;

import org.cloud13th.threads.normal.kmeans.common.data.DistanceMeasurer;
import org.cloud13th.threads.normal.kmeans.common.document.Document;
import org.cloud13th.threads.normal.kmeans.common.document.DocumentCluster;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

public class AssignmentTask extends RecursiveAction {
    private static final long serialVersionUID = -8709589012589400116L;
    // 任务要处理的文档数
    private final int start;
    private final int end;
    private final AtomicInteger numChanges;
    // 一个任务所能处理的最大文档数
    private final int maxSize;
    private DocumentCluster[] clusters;
    private Document[] documents;

    public AssignmentTask(DocumentCluster[] clusters, Document[] documents, int start, int end, AtomicInteger numChanges, int maxSize) {
        this.clusters = clusters;
        this.documents = documents;
        this.start = start;
        this.end = end;
        this.numChanges = numChanges;
        this.maxSize = maxSize;
    }

    @Override
    protected void compute() {
        // 检查任务必须处理的文 档数。如果该值小于或等于 maxSize 属性的值，那么处理这些文档
        if (end - start <= maxSize) {
            for (int i = start; i < end; i++) {
                Document document = documents[i];
                double distance = Double.MAX_VALUE;
                DocumentCluster selectedCluster = null;
                for (DocumentCluster cluster : clusters) {
                    // 计算每个文档和所有簇之间的欧氏距离，并且为文档选择距离最短的簇
                    double curDistance = DistanceMeasurer.euclideanDistance(document.getData(), cluster.getCentroid());
                    if (curDistance < distance) {
                        distance = curDistance;
                        selectedCluster = cluster;
                    }
                }
                assert selectedCluster != null;
                selectedCluster.addDocument(document);
                boolean result = document.setCluster(selectedCluster);
                if (result) {
                    numChanges.incrementAndGet();
                }
            }
        } else {
            // 如果该任务要处理的文档数量太多，那么将该集合分割成两个部分，并且创建两个新的任务来分别处理这两个部分
            int mid = (start + end) / 2;
            AssignmentTask task1 = new AssignmentTask(clusters, documents, start, mid, numChanges, maxSize);
            AssignmentTask task2 = new AssignmentTask(clusters, documents, mid, end, numChanges, maxSize);
            // invokeAll 方法在任务结束其执行后返回
            invokeAll(task1, task2);
        }
    }

    public DocumentCluster[] getClusters() {
        return clusters;
    }

    public void setClusters(DocumentCluster[] clusters) {
        this.clusters = clusters;
    }

    public Document[] getDocuments() {
        return documents;
    }

    public void setDocuments(Document[] documents) {
        this.documents = documents;
    }

}
