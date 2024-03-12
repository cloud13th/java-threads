package org.cloud13th.threads.normal.kmeans.serial;

import org.cloud13th.threads.normal.kmeans.common.data.DistanceMeasurer;
import org.cloud13th.threads.normal.kmeans.common.document.Document;
import org.cloud13th.threads.normal.kmeans.common.document.DocumentCluster;

import java.util.ArrayList;
import java.util.Random;

public class SerialKMeans {

    private SerialKMeans() {
    }

    public static DocumentCluster[] calculate(Document[] documents, int clusterCount, int vocSize, int seed) {
        // 创建一个由 clusterCount 参数确定的簇的数组，并且使用 initialize()方法和 Random 对象对其初始化
        DocumentCluster[] clusters = new DocumentCluster[clusterCount];
        Random random = new Random(seed);
        for (int i = 0; i < clusterCount; i++) {
            clusters[i] = new DocumentCluster(vocSize, new ArrayList<>());
            clusters[i].initialize(random);
        }
        // 重复指派和更新阶段，直到所有文档对应的簇都不再变化为止
        boolean change = true;
        while (change) {
            change = assignment(clusters, documents);
            update(clusters);
        }
        // 返回描述了文档最终组织情况的簇数组
        return clusters;
    }

    /**
     * 对于每个文档，该方法都计算其与所有簇之间的欧氏距离，并且将该文档指派到距离最短的簇。
     * 该方法返回一个布尔值，该值表明从当前位置到下一位置是否有一个或多个文档改变了为其指派的簇
     */
    private static boolean assignment(DocumentCluster[] clusters, Document[] documents) {
        for (DocumentCluster cluster : clusters) {
            cluster.clearClusters();
        }
        int numChanges = 0;
        for (Document document : documents) {
            double distance = Double.MAX_VALUE;
            DocumentCluster selectedCluster = null;
            for (DocumentCluster cluster : clusters) {
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
                numChanges++;
            }
        }
        return numChanges > 0;
    }

    private static void update(DocumentCluster[] clusters) {
        for (DocumentCluster cluster : clusters) {
            // 重新计算每个簇的质心
            cluster.calculateCentroid();
        }
    }

}
