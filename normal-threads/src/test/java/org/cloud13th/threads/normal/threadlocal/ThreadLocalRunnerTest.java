package org.cloud13th.threads.normal.threadlocal;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

class ThreadLocalRunnerTest {

    private static final String MARKET_CODE = "ML";

    void job() throws InterruptedException {
        var marketCode = RandomStringUtils.randomAlphabetic(5);
        var job = new ThreadLocalRunner();
        try {
            job.put(MARKET_CODE, marketCode);
            TimeUnit.MILLISECONDS.sleep(50);
            Assertions.assertEquals(marketCode, job.get(MARKET_CODE));
        } finally {
            job.clear();
        }
    }

    @Test
    void multiThreadRunner() {

    }
}
