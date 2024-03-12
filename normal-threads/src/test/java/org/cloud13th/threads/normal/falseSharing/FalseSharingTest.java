package org.cloud13th.threads.normal.falseSharing;

public class FalseSharingTest extends Thread {

    private static DataSimpleHolder dataHolder = new DataSimpleHolder();
    //    private static DataVolatileHolder dataHolder = new DataVolatileHolder();
//    private static DataVolatilePaddingHolder dataHolder = new DataVolatilePaddingHolder();
    private static long loops;

    public FalseSharingTest(Runnable runnable) {
        super(runnable);
    }

    public static void main(String[] args) throws InterruptedException {
        loops = Long.parseLong(args[0]);
        var threads = Integer.parseInt(args[1]);
        FalseSharingTest[] tests = new FalseSharingTest[4 * threads];
        for (int t = 0; t < threads; t++) {
            tests[t * 4] = new FalseSharingTest(() -> {
                for (int i = 0; i < loops; i++) {
                    dataHolder.l1 += i;
                }
            });
            tests[t * 4 + 1] = new FalseSharingTest(() -> {
                for (int i = 0; i < loops; i++) {
                    dataHolder.l2 += i;
                }
            });
            tests[t * 4 + 2] = new FalseSharingTest(() -> {
                for (int i = 0; i < loops; i++) {
                    dataHolder.l3 += i;
                }
            });
            tests[t * 4 + 3] = new FalseSharingTest(() -> {
                for (int i = 0; i < loops; i++) {
                    dataHolder.l4 += i;
                }
            });
        }
        // -0-
        long then = System.currentTimeMillis();
        for (FalseSharingTest test : tests) {
            test.start();
        }
        for (FalseSharingTest test : tests) {
            test.join();
        }
        long now = System.currentTimeMillis();
        System.out.println("Duration: " + (now - then) + " ms");
    }

    private static class DataSimpleHolder {
        private long l1 = 0;
        private long l2 = 0;
        private long l3 = 0;
        private long l4 = 0;
    }

    private static class DataVolatileHolder {
        private volatile long l1 = 0;
        private volatile long l2 = 0;
        private volatile long l3 = 0;
        private volatile long l4 = 0;
    }

    private static class DataVolatilePaddingHolder {
        private volatile long l1 = 0;
        private long[] dummy1 = new long[128 / 8];
        private volatile long l2 = 0;
        private long[] dummy2 = new long[128 / 8];
        private volatile long l3 = 0;
        private long[] dummy3 = new long[128 / 8];
        private volatile long l4 = 0;
    }
}
