package org.cloud13th.threads.normal.bestmatching;

import org.cloud13th.threads.normal.bestmatching.exist.concurrent.ExistConcurrentCalculation;
import org.cloud13th.threads.normal.bestmatching.exist.serial.ExistSerialCalculation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.concurrent.ExecutionException;

class ExistCalculationTest extends TestUtil {

    // =============================== for test ========================================
    protected static Boolean result;

    public static void main(String[] args) {
        double x = .9;
        // Speedup <= 1 / ((1 - P) + (P / N))
        System.out.println("Amdahl: " + (1 / (1 - x) + (x + NUM_CORES)));
        // Speedup = P - N * （P - 1)
        System.out.println("Gustafson: " + (x - NUM_CORES * (x - 1)));
        // Speedup = t_sequence / t_parallel
        System.out.println("SpeedUp: " + (183 / 71));
    }

    @AfterAll
    static void summary() {
        System.out.println("\n\n==============================");
        System.out.println("Word: " + TARGET_WORD);
        System.out.println("Exists: " + result);
        System.out.println("Cores: " + NUM_CORES);
        System.out.println("Execution Time: " + executionTimeArray);
        System.out.println("Average Execution Time: " + executionTimeArray.stream().reduce(0L, Long::sum) / (REPEATED_TIMES));
        System.out.println("------------------------------");
        System.out.println("==============================\n\n");
    }

    @RepeatedTest(value = REPEATED_TIMES)
    @DisplayName("serial version")
    void serial() {
        result = ExistSerialCalculation.existWord(TARGET_WORD, DICTIONARY); // 183
    }

    @RepeatedTest(value = REPEATED_TIMES)
    @DisplayName("concurrent version")
    void concurrent() throws ExecutionException, InterruptedException {
        result = ExistConcurrentCalculation.existWord(TARGET_WORD, DICTIONARY, NUM_CORES); // 71
    }
}
