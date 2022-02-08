package com.example.banking.executors;

import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

@Component
public class SinglethreadedService extends Runner {
    public SinglethreadedService() {
        super(Executors.newSingleThreadExecutor());
    }
}
