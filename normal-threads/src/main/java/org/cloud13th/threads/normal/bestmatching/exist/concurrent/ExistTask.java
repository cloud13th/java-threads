package org.cloud13th.threads.normal.bestmatching.exist.concurrent;

import org.cloud13th.threads.normal.bestmatching.common.LevenshteinDistance;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

public class ExistTask implements Callable<Boolean> {

    private final int startIndex;

    private final int endIndex;

    private final List<String> dictionary;

    private final String word;

    public ExistTask(int startIndex, int endIndex, List<String> dictionary, String word) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.dictionary = dictionary;
        this.word = word;
    }

    @Override
    public Boolean call() {
        for (int i = startIndex; i < endIndex; i++) {
            if (LevenshteinDistance.calculate(word, dictionary.get(i)) == 0) {
                return true;
            }

            if (Thread.interrupted()) {
                return false;
            }
        }
        throw new NoSuchElementException("The word " + word + " doesn't exists.");
    }

}
