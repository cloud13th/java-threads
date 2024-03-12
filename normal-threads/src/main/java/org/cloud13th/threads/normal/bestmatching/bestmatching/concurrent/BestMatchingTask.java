package org.cloud13th.threads.normal.bestmatching.bestmatching.concurrent;

import org.cloud13th.threads.normal.bestmatching.common.BestMatchingData;
import org.cloud13th.threads.normal.bestmatching.common.LevenshteinDistance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class BestMatchingTask implements Callable<BestMatchingData> {

    private final int startIndex;

    private final int endIndex;

    private final List<String> dictionary;

    private final String word;

    public BestMatchingTask(int startIndex, int endIndex, List<String> dictionary, String word) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.dictionary = dictionary;
        this.word = word;
    }

    @Override
    public BestMatchingData call() {
        List<String> results = new ArrayList<>();
        int minDistance = Integer.MAX_VALUE;
        int distance;
        for (int i = startIndex; i < endIndex; i++) {
            distance = LevenshteinDistance.calculate(word, dictionary.get(i));
            if (distance < minDistance) {
                results.clear();
                minDistance = distance;
                results.add(dictionary.get(i));
            } else if (distance == minDistance) {
                results.add(dictionary.get(i));
            }
        }

        BestMatchingData result = new BestMatchingData();
        result.setWords(results);
        result.setDistance(minDistance);
        return result;
    }

}
