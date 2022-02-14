package com.example.banking.utils;

import java.util.List;
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

    public static List<String> emails = List.of("aamir@gmail.com", "ananya@gmail.com", "bob@gmail.com", "bret@gmail.com", "chris@gmail.com", "jack@gmail.com", "jimmy@gmail.com", "john@gmail.com", "mark@gmail.com", "mike@gmail.com", "ram@gmail.com", "rob@gmail.com", "rohan@gmail.com", "sam@gmail.com", "shyam@gmail.com", "sita@gmail.com", "sohan@gmail.com", "ted@gmail.com", "tim@gmail.com", "tom@gmail.com");


    public static long getRandomTransactionAmount(long max) {
        return Math.abs(random.nextLong()%(max*2+1)) - max;
    }

    public static int randomInt(int value) {
        return random.nextInt(value);
    }

}
