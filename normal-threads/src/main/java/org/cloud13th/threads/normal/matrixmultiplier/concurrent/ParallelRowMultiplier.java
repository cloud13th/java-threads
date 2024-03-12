package org.cloud13th.threads.normal.matrixmultiplier.concurrent;

import java.util.ArrayList;
import java.util.List;

public class ParallelRowMultiplier {

    private ParallelRowMultiplier() {
    }

    public static void multiply(double[][] matrix1, double[][] matrix2, double[][] result) {
        List<Thread> threads = new ArrayList<>();
        int numThreads = Runtime.getRuntime().availableProcessors() * 2;

        // 每行一个线程
        for (int i = 0; i < matrix1.length; i++) {
            RowMultiplierTask task = new RowMultiplierTask(result, matrix1, matrix2, i);
            Thread thread = new Thread(task);
            thread.start();
            threads.add(thread);
            // 资源不足的情况
            // 防止由于过多的线程导致系统超载，这里线性执行每组线程，线程数=CPU核数*2（也可以是其他的值，这里主要是防止系统挂掉）
            if (threads.size() % numThreads == 0) {
                waitForThreads(threads);
            }
        }
        waitForThreads(threads);
    }

    private static void waitForThreads(List<Thread> threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        threads.clear();
    }

}

class RowMultiplierTask implements Runnable {

    private final double[][] result;
    private final double[][] matrix1;
    private final double[][] matrix2;

    private final int row;

    public RowMultiplierTask(double[][] result, double[][] matrix1, double[][] matrix2, int i) {
        this.result = result;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.row = i;
    }

    public void run() {
        for (int j = 0; j < matrix2[0].length; j++) {
            result[row][j] = 0;
            for (int k = 0; k < matrix1[row].length; k++) {
                result[row][j] += matrix1[row][k] * matrix2[k][j];
            }
        }
    }

}
