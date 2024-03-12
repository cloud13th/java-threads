package org.cloud13th.threads.normal.matrixmultiplier.concurrent;

import java.util.ArrayList;
import java.util.List;

public class ParallelIndividualMultiplier {

    private ParallelIndividualMultiplier() {
    }

    public static void multiply(double[][] matrix1, double[][] matrix2, double[][] result) {
        List<Thread> threads = new ArrayList<>();

        // 每个元素一个线程
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                IndividualMultiplierTask task = new IndividualMultiplierTask(result, matrix1, matrix2, i, j);
                Thread thread = new Thread(task);
                thread.start();
                threads.add(thread);
                // 资源不足的情况
                // 防止由于过多的线程导致系统超载，这里线性执行每组线程，线程数10（方便计算）
                if (threads.size() % 10 == 0) {
                    waitForThreads(threads);
                }
            }
        }
    }

    private static void waitForThreads(List<Thread> threads) {
        System.out.println("Thread waiting: " + threads.size());
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

class IndividualMultiplierTask implements Runnable {

    private final double[][] result;
    private final double[][] matrix1;
    private final double[][] matrix2;

    private final int row;
    private final int column;

    public IndividualMultiplierTask(double[][] result, double[][] matrix1, double[][] matrix2, int i, int j) {
        this.result = result;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.row = i;
        this.column = j;
    }

    public void run() {
        result[row][column] = 0;
        for (int k = 0; k < matrix1[row].length; k++) {
            result[row][column] += matrix1[row][k] * matrix2[k][column];
        }
    }

}
