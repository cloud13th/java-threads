package org.cloud13th.threads.normal.matrixmultiplier.concurrent;

import java.util.ArrayList;
import java.util.List;

public class ParallelGroupMultiplier {

    private ParallelGroupMultiplier() {
    }

    public static void multiply(double[][] matrix1, double[][] matrix2, double[][] result) {
        List<Thread> threads = new ArrayList<>();

        int rows1 = matrix1.length;

        int numThreads = Runtime.getRuntime().availableProcessors();
        int startIndex = 0;
        int step = rows1 / numThreads;
        int endIndex = step;

        // 线程的数量由处理器决定
        for (int i = 0; i < numThreads; i++) {
            GroupMultiplierTask task = new GroupMultiplierTask(result, matrix1, matrix2, startIndex, endIndex);
            Thread thread = new Thread(task);
            thread.start();
            threads.add(thread);
            startIndex = endIndex;
            endIndex = i == numThreads - 2 ? rows1 : endIndex + step;
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

class GroupMultiplierTask implements Runnable {

    private final double[][] result;
    private final double[][] matrix1;
    private final double[][] matrix2;

    private final int startIndex;
    private final int endIndex;

    public GroupMultiplierTask(double[][] result, double[][] matrix1, double[][] matrix2, int startIndex, int endIndex) {
        this.result = result;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public void run() {
        for (int i = startIndex; i < endIndex; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                result[i][j] = 0;
                for (int k = 0; k < matrix1[i].length; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
    }
}
