package com.example.banking.services;

import com.example.banking.executors.DistributedScheduledService;
import com.example.banking.executors.MultithreadedService;
import com.example.banking.executors.ScheduledService;
import com.example.banking.models.Notification;
import com.example.banking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NotifierService {

//    private final ConcurrentHashMap<String, Notification> notifications;
    private final ScheduledService scheduler;

    private final ConcurrentLinkedQueue<Notification> queuedNotifications, failedNotifications;

    private final Integer FAILED_NOTIFICATION_LOCK = 1;
    private final Integer NEW_NOTIFICATION_LOCK = 0;

//    private final ConcurrentHashMap<String,Notification> failedNotifications;

    @Autowired
    private MultithreadedService multithreadedService;

    @Autowired
    private DistributedScheduledService distributedScheduledService;

    private final AtomicInteger skipCount;
    private final int NOTIFICATION_THRESHOLD = 10;
    private final int SKIP_COUNT_THRESHOLD = 5;

//    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<Notification>> notificationQueues;

    public NotifierService() {
//        notifications = new ConcurrentHashMap<>();
        scheduler = new ScheduledService();
//        scheduler = new DistributedScheduledService();
        skipCount = new AtomicInteger(0);

//        notificationQueues = new ConcurrentHashMap<>();

        failedNotifications = new ConcurrentLinkedQueue<>();
        queuedNotifications = new ConcurrentLinkedQueue<>();
    }

//    public void registerUser(String email) {
//        notificationQueues.putIfAbsent(email,new ConcurrentLinkedQueue<>());
//    }
//
//    public void add(String email,Notification notification) {
//        synchronized(NEW_NOTIFICATION_LOCK) {
//            notificationQueues.get(email).add(notification);
//        }
//    }

    public void add(Notification notification) {
        synchronized (NEW_NOTIFICATION_LOCK) {
            queuedNotifications.add(notification);
        }
    }

    private void resetSkipCount() {
        this.skipCount.set(0);
    }

    private void handleFailure(Notification notification) {
        System.out.println("#### " + notification.getEmail() + " failed ####");
        synchronized (FAILED_NOTIFICATION_LOCK) {
            failedNotifications.add(notification);
        }
    }

    private void api(Notification notification) {

        distributedScheduledService.submit(
                notification.getEmail(),
                () -> {
                    if(Utils.randomInt(100) > 60) {
                        handleFailure(notification);
                        return;
                    }
                    System.out.println("*****" + notification + "*****");
                }
        );

    }

    public void sendFailedNotifications() {
        System.out.println("RETRYING SENDING FAILED NOTIFICATIONS...");

        List<Notification> notificationList;
        synchronized (FAILED_NOTIFICATION_LOCK) {
            notificationList = new ArrayList<>(this.failedNotifications);
            this.failedNotifications.clear();
        }
        notificationList.forEach(this::api);
//        notificationList.forEach(
//                notification -> distributedScheduledService.submit(
//                        notification.getEmail(),() -> api(notification)
//                )
//        );
    }

    public void sendNotifications() {

        System.out.println("SENDING NOTIFICATIONS...");
        System.out.println("FAILED - " + failedNotifications.size());
        System.out.println("QUEUED - " + queuedNotifications.size());

        if(this.notEnoughNotifications()) {
            skipCount.incrementAndGet();
            return;
        }

        List<Notification> notifications;
        synchronized (NEW_NOTIFICATION_LOCK) {
            notifications = new ArrayList<>(queuedNotifications);
            queuedNotifications.clear();
            resetSkipCount();
        }

        notifications.forEach(this::api);
    }

    private boolean notEnoughNotifications() {
        return this.queuedNotifications.size() < NOTIFICATION_THRESHOLD
                && skipCount.intValue() < SKIP_COUNT_THRESHOLD;
    }

    public void run() {
        scheduler.schedule(
                this::sendNotifications,
                10,
                TimeUnit.SECONDS
        );

        scheduler.schedule(
                this::sendFailedNotifications,
                30,
                TimeUnit.SECONDS
        );
    }
}