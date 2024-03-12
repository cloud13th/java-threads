package org.cloud13th.threads.normal.bestmatching.exist.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ExistConcurrentCalculation {

    private ExistConcurrentCalculation() {
    }

    public static boolean existWord(String word, List<String> dictionary, int numCores) throws InterruptedException, ExecutionException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numCores);

        int size = dictionary.size();
        int step = size / numCores;
        int startIndex;
        int endIndex;
        List<ExistTask> tasks = new ArrayList<>();

        for (int i = 0; i < numCores; i++) {
            startIndex = i * step;
            if (i == numCores - 1) {
                endIndex = dictionary.size();
            } else {
                endIndex = (i + 1) * step;
            }
            ExistTask task = new ExistTask(startIndex, endIndex, dictionary, word);
            tasks.add(task);
        }
        try {
            return executor.invokeAny(tasks);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof NoSuchElementException) {
                return false;
            }
            throw e;
        } finally {
            executor.shutdown();
        }
    }
}
