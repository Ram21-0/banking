package com.example.banking.notif;

import com.example.banking.models.Notification;
import com.example.banking.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class Notifier<T extends Notification> implements Runnable {

    private final ConcurrentHashMap<String,T> notifications;
    private final ScheduledExecutorService executorService;
    private final AtomicInteger skipCount;

    public Notifier() {
        notifications = new ConcurrentHashMap<>();
        executorService = Executors.newScheduledThreadPool(Utils.POOL_SIZE);
        skipCount = new AtomicInteger(0);
    }

    public void add(T notification) {
        notifications.put(notification.getEmail(),notification);
    }

    public void flush() {
        notifications.clear();
    }

    public List<T> pollAllNotifications() {
        List<T> notificationList;
        synchronized (this) {
            notificationList = new ArrayList<>(notifications.values());
            this.skipCount.set(0);
            this.flush();
        }
        Collections.sort(notificationList);

        return notificationList;
    }

    public void printNotifications() {
        if(this.notEnoughNotifications()) {
            skipCount.incrementAndGet();
            return;
        }

        List<T> notificationList = pollAllNotifications();
        System.out.println(
                notificationList.stream()
                        .map(Notification::toString)
                        .collect(Collectors.joining("\n", "--------- NOTIFICATIONS ---------\n", "\n---------------------------------"))
        );
    }

    private boolean notEnoughNotifications() {
        return this.notifications.size() < 10 && skipCount.intValue() < 5;
    }

    @Override
    public void run() {
        executorService.scheduleWithFixedDelay(
                this::printNotifications,
                10,
                10,
                TimeUnit.SECONDS
        );
    }
}