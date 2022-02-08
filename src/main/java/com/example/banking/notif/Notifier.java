package com.example.banking.notif;

import com.example.banking.models.Notification;
import com.example.banking.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class Notifier<T extends Notification> implements Runnable {

    private final ConcurrentHashMap<String,T> notifications;
    private final ScheduledExecutorService executorService;

    public Notifier() {
        notifications = new ConcurrentHashMap<>();
        executorService = Executors.newScheduledThreadPool(Utils.POOL_SIZE);
    }

    public void add(T notification) {
        notifications.put(notification.getEmail(),notification);
    }

    public void flush() {
        notifications.clear();
    }

    public List<T> getAllNotifications() {
        List<T> notificationList;
        synchronized (this) {
            notificationList = new ArrayList<>(notifications.values());
            this.flush();
        }
        Collections.sort(notificationList);

        return notificationList;
    }

    public void printNotifications() {
        if(this.notEnoughNotifications()) {
            return;
        }

        List<T> list = getAllNotifications();
        System.out.println(
                list.stream()
                        .map(Notification::toString)
                        .collect(Collectors.joining("\n", "--------- NOTIFICATIONS ---------\n", "\n---------------------------------"))
        );
    }

    private boolean notEnoughNotifications() {
        return this.notifications.size() < 10;
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