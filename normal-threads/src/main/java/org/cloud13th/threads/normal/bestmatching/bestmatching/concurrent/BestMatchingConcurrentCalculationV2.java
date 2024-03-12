package org.cloud13th.threads.normal.bestmatching.bestmatching.concurrent;

import org.cloud13th.threads.normal.bestmatching.common.BestMatchingData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class BestMatchingConcurrentCalculationV2 {

    private BestMatchingConcurrentCalculationV2() {
    }

    public static BestMatchingData getBestMatchingWords(String word, List<String> dictionary, int numCores) throws InterruptedException, ExecutionException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numCores);

        int size = dictionary.size();
        int step = size / numCores;
        int startIndex;
        int endIndex;
        List<BestMatchingTask> tasks = new ArrayList<>();

        for (int i = 0; i < numCores; i++) {
            startIndex = i * step;
            if (i == numCores - 1) {
                endIndex = dictionary.size();
            } else {
                endIndex = (i + 1) * step;
            }
            BestMatchingTask task = new BestMatchingTask(startIndex, endIndex, dictionary, word);
            tasks.add(task);
        }
        List<Future<BestMatchingData>> results = executor.invokeAll(tasks);
        executor.shutdown();

        List<String> words = new ArrayList<>();
        int minDistance = Integer.MAX_VALUE;
        for (Future<BestMatchingData> future : results) {
            BestMatchingData data = future.get();
            if (data.getDistance() < minDistance) {
                words.clear();
                minDistance = data.getDistance();
                words.addAll(data.getWords());
            } else if (data.getDistance() == minDistance) {
                words.addAll(data.getWords());
            }
        }

        BestMatchingData result = new BestMatchingData();
        result.setDistance(minDistance);
        result.setWords(words);
        return result;

    }

}
