package com.example.banking.executors;

import com.example.banking.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ScheduledService {

    private final ScheduledExecutorService scheduler;

    public ScheduledService() {
        this.scheduler = Executors.newScheduledThreadPool(Utils.POOL_SIZE);
    }

    public void schedule(Runnable command, int delay, TimeUnit unit) {
        scheduler.scheduleWithFixedDelay(command,0,delay,unit);
    }

    public void scheduleRandomly(Runnable command,int maximumDelay) {
        scheduler.schedule(
                () -> {
                    command.run();
                    scheduleRandomly(command,maximumDelay);
                },
                Utils.randomInt(maximumDelay),
                TimeUnit.SECONDS
        );
    }
}
