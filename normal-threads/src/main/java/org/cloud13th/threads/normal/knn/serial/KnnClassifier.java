package org.cloud13th.threads.normal.knn.serial;

import org.cloud13th.threads.normal.knn.common.data.Distance;
import org.cloud13th.threads.normal.knn.common.data.Sample;
import org.cloud13th.threads.normal.knn.common.distances.EuclideanDistanceCalculator;

import java.util.*;

/**
 * Serial implementation of the Knn algorithm
 *
 * @author author
 */
public class KnnClassifier {

    /**
     * List of train data
     */
    private final List<? extends Sample> dataSet;

    /**
     * K parameter
     */
    private final int k;

    /**
     * Constructor of the class. Initialize the internal data
     *
     * @param dataSet Train data
     * @param k       K parameter
     */
    public KnnClassifier(List<? extends Sample> dataSet, int k) {
        this.dataSet = dataSet;
        this.k = k;
    }

    /**
     * Method that classifies an example
     *
     * @param example Example to classify
     * @return The tag or class of the example
     * @throws Exception throw an Exception if something goes wrong
     */
    public String classify(Sample example) {

        Distance[] distances = new Distance[dataSet.size()];

        int index = 0;

        for (Sample localExample : dataSet) {
            distances[index] = new Distance();
            distances[index].setIndex(index);
            distances[index].setDistance(EuclideanDistanceCalculator.calculate(localExample, example));
            index++;
        }

        Arrays.sort(distances);

        Map<String, Integer> results = new HashMap<>();
        for (int i = 0; i < k; i++) {
            Sample localExample = dataSet.get(distances[i].getIndex());
            String tag = localExample.getTag();
            results.merge(tag, 1, Integer::sum);
        }


        return Collections.max(results.entrySet(),
                Map.Entry.comparingByValue()).getKey();
    }
}
