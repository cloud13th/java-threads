package org.cloud13th.threads.normal.kmeans;

import org.cloud13th.threads.normal.CommonTestUtil;
import org.cloud13th.threads.normal.kmeans.common.data.VocabularyLoader;
import org.cloud13th.threads.normal.kmeans.common.document.Document;
import org.cloud13th.threads.normal.kmeans.common.document.DocumentCluster;
import org.cloud13th.threads.normal.kmeans.common.document.DocumentLoader;
import org.cloud13th.threads.normal.kmeans.parallel.ConcurrentKMeans;
import org.cloud13th.threads.normal.kmeans.serial.SerialKMeans;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

class KmeansTest extends CommonTestUtil {

    // 参数 K 确定了要生成的簇数
    private static final int K = 10;
    // 随机数生成器的“种子”。该“种子”确定了初始质心的位置
    private static final int SEED = 13;
    // maxSize 参数确定了一个任务在不分割的前提下所能处理的最大项(文档或者簇)数
    private static final int MAX_SIZE = 10;
    private static final List<Long> executionTimeArray = new ArrayList<>();
    private static Map<String, Integer> vocIndex;


    // =============================== for test ========================================
    private static Document[] documents;
    private static DocumentCluster[] clusters;
    private static Date startTime;

    @BeforeAll
    static void loadData() throws IOException {
        Path pathVoc = Paths.get("data", "kmeans-movies.words");
        vocIndex = VocabularyLoader.load(pathVoc);
        Path pathDocs = Paths.get("data", "kmeans-movies.data");
        documents = DocumentLoader.load(pathDocs, vocIndex);
    }

    @AfterAll
    static void summary() {
        System.out.println("\n\n==============================");
        System.out.println("K: " + K + "; SEED: " + SEED);
        System.out.println("Execution Time: " + executionTimeArray);
        System.out.println("Average Execution Time: " +
                executionTimeArray.stream()
                        .mapToLong(Long::valueOf)
                        .sum() / REPEATED_TIMES);
        System.out.println(Arrays.stream(clusters)
                .map(DocumentCluster::getDocumentCount)
                .sorted(Comparator.reverseOrder())
                .map(Object::toString)
                .collect(Collectors.joining(", ", "Cluster sizes: ", ""))
        );
        System.out.println("==============================\n\n");
    }

    @RepeatedTest(REPEATED_TIMES)
    @DisplayName("serial version")
    void serial() {
        clusters = SerialKMeans.calculate(documents, K, vocIndex.size(), SEED);
    }

    @RepeatedTest(REPEATED_TIMES)
    @DisplayName("parallel version")
    void parallel() {
        clusters = ConcurrentKMeans.calculate(documents, K, vocIndex.size(), SEED, MAX_SIZE);
    }

    @BeforeEach
    void preAction() {
        startTime = new Date();
    }

    @AfterEach
    void afterAction() {
        executionTimeArray.add(new Date().getTime() - startTime.getTime());
    }
}
