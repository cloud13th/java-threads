package org.cloud13th.threads.normal.knn;

import org.cloud13th.threads.normal.CommonTestUtil;
import org.cloud13th.threads.normal.knn.common.data.BankMarketing;
import org.cloud13th.threads.normal.knn.common.loader.BankMarketingLoader;
import org.cloud13th.threads.normal.knn.parallel.KnnClassifierParallelGroup;
import org.cloud13th.threads.normal.knn.parallel.KnnClassifierParallelIndividual;
import org.cloud13th.threads.normal.knn.serial.KnnClassifier;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class KnnTest extends CommonTestUtil {

    private static final int k = 10;
    protected static List<BankMarketing> train;
    protected static List<BankMarketing> test;
    protected static List<Long> executionTimeArray = new ArrayList<>();


    // =============================== for test ========================================
    private static int success;
    private static int mistakes;
    private Date startTime;

    @BeforeAll
    static void loadData() {
        BankMarketingLoader loader = new BankMarketingLoader();
        train = loader.load("data/knn-bank.data");
        test = loader.load("data/knn-bank.test");
    }

    @AfterAll
    static void summary() {
        System.out.println("\n\n==============================");
        System.out.println("Train: " + train.size());
        System.out.println("Test: " + test.size());
        System.out.println("Classifier - K: " + k);
        System.out.println("Success: " + success);
        System.out.println("Mistakes: " + mistakes);
        System.out.println("Execution Time: " + executionTimeArray);
        System.out.println("Average Execution Time: " + executionTimeArray.stream().reduce(0L, Long::sum) / (REPEATED_TIMES));
        System.out.println("==============================\n\n");
    }

    @RepeatedTest(REPEATED_TIMES)
    @DisplayName("serial version")
    void serial() {
        KnnClassifier classifier = new KnnClassifier(train, k);
        for (BankMarketing ex : test) {
            String tag = classifier.classify(ex);
            if (tag.equals(ex.getTag())) {
                success++;
            } else {
                mistakes++;
            }
        }
    }

    @RepeatedTest(REPEATED_TIMES)
    @DisplayName("parallel individual version")
    void parallelIndividual() throws InterruptedException {
        boolean sort = false;
        KnnClassifierParallelIndividual classifier = new KnnClassifierParallelIndividual(train, k, 1, sort);
        for (BankMarketing ex : test) {
            String tag = classifier.classify(ex);
            if (tag.equals(ex.getTag())) {
                success++;
            } else {
                mistakes++;
            }
        }
    }

    @RepeatedTest(REPEATED_TIMES)
    @DisplayName("parallel group version")
    void parallelGroup() throws InterruptedException {
        boolean sort = false;
        KnnClassifierParallelGroup classifier = new KnnClassifierParallelGroup(train, k, sort);
        for (BankMarketing ex : test) {
            String tag = classifier.classify(ex);
            if (tag.equals(ex.getTag())) {
                success++;
            } else {
                mistakes++;
            }
        }
    }

    @BeforeEach
    void preAction() {
        success = 0;
        mistakes = 0;
        startTime = new Date();
    }

    @AfterEach
    void afterAction() {
        executionTimeArray.add(new Date().getTime() - startTime.getTime());
    }
}
