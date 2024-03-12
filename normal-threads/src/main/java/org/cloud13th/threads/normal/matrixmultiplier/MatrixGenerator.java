package org.cloud13th.threads.normal.matrixmultiplier;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MatrixGenerator {

    private static final Random RANDOM = ThreadLocalRandom.current();

    private MatrixGenerator() {
    }

    public static double[][] generate(int rows, int columns) {
        double[][] matrix = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = RANDOM.nextDouble() * 10;
            }
        }
        return matrix;
    }
}
