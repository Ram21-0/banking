package com.example.banking.utils;

import java.util.Random;

public class Utils {

    private static final Random random = new Random();
    public static final int POOL_SIZE = Runtime.getRuntime().availableProcessors();

    public static long calculateExecutionTime(Runnable runnable) {
        long start = System.nanoTime();
        runnable.run();
        long end = System.nanoTime();
        return end - start;
    }

    public static long getRandomTransactionAmount(long max) {
        return Math.abs(random.nextLong()%(max*2+1)) - max;
    }

    public static int randomInt(int value) {
        return random.nextInt(value);
    }

}
