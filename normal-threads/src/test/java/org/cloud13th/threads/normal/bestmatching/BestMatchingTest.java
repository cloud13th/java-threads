package org.cloud13th.threads.normal.bestmatching;

import org.cloud13th.threads.normal.bestmatching.bestmatching.concurrent.BestMatchingConcurrentCalculation;
import org.cloud13th.threads.normal.bestmatching.bestmatching.concurrent.BestMatchingConcurrentCalculationV2;
import org.cloud13th.threads.normal.bestmatching.bestmatching.serial.BestMatchingSerialCalculation;
import org.cloud13th.threads.normal.bestmatching.common.BestMatchingData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.List;
import java.util.concurrent.ExecutionException;

class BestMatchingTest extends TestUtil {

    protected static Integer minimumDistance;
    protected static List<String> results;

    public static void main(String[] args) {
        System.out.println("\n\n------------------------------");
        double P = .7;
        // Speedup <= 1 / ((1 - P) + (P / N))
        System.out.println("Amdahl: " + (1 / (1 - P) + (P + NUM_CORES)));
        // Speedup = P - N * （P - 1)
        System.out.println("Gustafson: " + (P - NUM_CORES * (P - 1)));
        // todo Speedup = t_sequence / t_parallel
        System.out.println("SpeedUp: " + 155 / 41);
        System.out.println("------------------------------\n\n");
    }

    @AfterAll
    static void summary() {
        System.out.println("\n\n==============================");
        System.out.println("Word: " + TARGET_WORD);
        System.out.println("Minimum distance: " + minimumDistance);
        System.out.println("List of best matching words: " + results.size());
        System.out.println(results);
        System.out.println("Cores: " + NUM_CORES);
        System.out.println("Execution Time: " + executionTimeArray);
        System.out.println("Average Execution Time: " + executionTimeArray.stream().reduce(0L, Long::sum) / (REPEATED_TIMES));
        System.out.println("==============================\n\n");
    }

    // =============================== for test ========================================

    @RepeatedTest(value = REPEATED_TIMES)
    @DisplayName("serial version")
    void serial() {
        BestMatchingData result = BestMatchingSerialCalculation.getBestMatchingWords(TARGET_WORD, DICTIONARY);
        results = result.getWords();
        minimumDistance = result.getDistance();
    }

    @RepeatedTest(value = REPEATED_TIMES)
    @DisplayName("concurrent V1")
    void concurrentV1() throws ExecutionException, InterruptedException {
        BestMatchingData result = BestMatchingConcurrentCalculation.getBestMatchingWords(TARGET_WORD, DICTIONARY, NUM_CORES);
        results = result.getWords();
        minimumDistance = result.getDistance();
    }

    @RepeatedTest(value = REPEATED_TIMES)
    @DisplayName("concurrent V2")
    void concurrentV2() throws ExecutionException, InterruptedException {
        BestMatchingData result = BestMatchingConcurrentCalculationV2.getBestMatchingWords(TARGET_WORD, DICTIONARY, NUM_CORES);
        results = result.getWords();
        minimumDistance = result.getDistance();
    }
}
