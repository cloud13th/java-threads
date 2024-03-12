package org.cloud13th.threads.normal.bestmatching.bestmatching.serial;

import org.cloud13th.threads.normal.bestmatching.common.BestMatchingData;
import org.cloud13th.threads.normal.bestmatching.common.LevenshteinDistance;

import java.util.ArrayList;
import java.util.List;

public class BestMatchingSerialCalculation {

    private BestMatchingSerialCalculation() {
    }

    public static BestMatchingData getBestMatchingWords(String word, List<String> dictionary) {
        List<String> results = new ArrayList<>();
        int minDistance = Integer.MAX_VALUE;
        int distance;

        for (String str : dictionary) {
            distance = LevenshteinDistance.calculate(word, str);
            if (distance < minDistance) {
                results.clear();
                minDistance = distance;
                results.add(str);
            } else if (distance == minDistance) {
                results.add(str);
            }
        }

        BestMatchingData result = new BestMatchingData();
        result.setWords(results);
        result.setDistance(minDistance);
        return result;
    }

}
