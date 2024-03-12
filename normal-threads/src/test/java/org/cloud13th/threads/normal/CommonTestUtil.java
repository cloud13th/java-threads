package org.cloud13th.threads.normal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommonTestUtil {

    protected static final int REPEATED_TIMES = 3;
    protected static final int NUM_CORES = Runtime.getRuntime().availableProcessors();
    //    protected static final int NUM_CORES = 1000;
    protected static List<Long> executionTimeArray = new ArrayList<>();
    protected Date startTime;

    @BeforeEach
    void beforeAction() {
        startTime = new Date();
    }

    @AfterEach
    void afterAction() {
        Date endTime = new Date();
        executionTimeArray.add(endTime.getTime() - startTime.getTime());
    }
}
