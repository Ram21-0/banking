package com.example.banking.executors;

import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

@Component
public class MultithreadedService extends Runner {
    public MultithreadedService() {
        super(Executors.newFixedThreadPool(10));
    }
}
