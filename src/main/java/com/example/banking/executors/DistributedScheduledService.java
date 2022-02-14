package com.example.banking.executors;

import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class DistributedScheduledService {

    private final List<ExecutorService> services;
    private final int SIZE = 4;

    private ExecutorService newService(int i) {
        return Executors.newCachedThreadPool();
//        return new ScheduledService();
    }

    public DistributedScheduledService() {
        services = IntStream.rangeClosed(1,SIZE)
                .mapToObj(this::newService)
                .collect(Collectors.toList());
    }

    public void submit(String email, Runnable command) {
        int schedulerId = Math.abs(email.hashCode()) % SIZE;
        System.out.println(email + ": " + schedulerId);
        services.get(schedulerId).submit(command);
    }
}
