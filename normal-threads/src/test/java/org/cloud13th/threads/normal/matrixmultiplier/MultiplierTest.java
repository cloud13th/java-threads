package org.cloud13th.threads.normal.matrixmultiplier;

import org.cloud13th.threads.normal.CommonTestUtil;
import org.cloud13th.threads.normal.matrixmultiplier.concurrent.ParallelGroupMultiplier;
import org.cloud13th.threads.normal.matrixmultiplier.concurrent.ParallelIndividualMultiplier;
import org.cloud13th.threads.normal.matrixmultiplier.concurrent.ParallelRowMultiplier;
import org.cloud13th.threads.normal.matrixmultiplier.serial.SerialMultiplier;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

class MultiplierTest extends CommonTestUtil {

    private static double[][] matrix1;
    private static double[][] matrix2;
    private static double[][] result;

    @BeforeAll
    static void init() {
        matrix1 = MatrixGenerator.generate(1000, 1000);
        matrix2 = MatrixGenerator.generate(1000, 1000);
        result = new double[matrix1.length][matrix2[0].length];
    }

    @AfterAll
    static void summary() {
        System.out.println("\n\n==============================");
        System.out.println("Cores: " + NUM_CORES);
        System.out.println("Execution Time: " + executionTimeArray);
        System.out.println("Average Execution Time: " + executionTimeArray.stream().reduce(0L, Long::sum) / (REPEATED_TIMES));
        System.out.println("==============================\n\n");
    }

    @RepeatedTest(REPEATED_TIMES)
    @DisplayName("serial version")
    void serial() {
        SerialMultiplier.multiply(matrix1, matrix2, result); // 6190
    }

    @RepeatedTest(REPEATED_TIMES)
    @DisplayName("parallel individual version")
    void parallelIndividualVersion() {
        // 每个元素开启一个线程
        ParallelIndividualMultiplier.multiply(matrix1, matrix2, result);
    }


    // =============================== for test ========================================

    @RepeatedTest(REPEATED_TIMES)
    @DisplayName("parallel row version")
    void parallelRowVersion() {
        // 每行开启一个线程
        ParallelRowMultiplier.multiply(matrix1, matrix2, result);
    }

    @RepeatedTest(REPEATED_TIMES)
    @DisplayName("parallel group version")
    void parallelGroupVersion() {
        // 线程的数量由处理器决定
        ParallelGroupMultiplier.multiply(matrix1, matrix2, result);
    }
}
