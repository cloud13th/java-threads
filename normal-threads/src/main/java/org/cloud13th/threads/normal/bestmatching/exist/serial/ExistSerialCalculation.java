package org.cloud13th.threads.normal.bestmatching.exist.serial;

import org.cloud13th.threads.normal.bestmatching.common.LevenshteinDistance;

import java.util.List;

public class ExistSerialCalculation {

    private ExistSerialCalculation() {
    }

    public static boolean existWord(String word, List<String> dictionary) {
        for (String str : dictionary) {
            if (LevenshteinDistance.calculate(word, str) == 0) {
                return true;
            }
        }
        return false;
    }
}
