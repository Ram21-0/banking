package com.example.banking.services;

import com.example.banking.executors.MultithreadedService;
import com.example.banking.executors.ScheduledService;
import com.example.banking.models.Notification;
import com.example.banking.models.User;
import com.example.banking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

import static com.example.banking.utils.Utils.emails;

@Service
public class Service2 {

    private final String SERVICE_TYPE = "TYPE_2";

    @Autowired
    private ScheduledService scheduledService;

    @Autowired
    private NotifierService notifier;

    @Autowired
    private UserRepository repository;

    @Autowired
    private MultithreadedService multithreadedService;

    public void run() {

        for (String email : emails) {
            scheduledService.scheduleRandomly(
                    () -> notifier.add(makeNotification(email)),
                    60
            );
        }

    }

    public Notification makeNotification(String email) {
        Notification.NotificationBuilder builder = new Notification.NotificationBuilder(email);
        try {
            builder.balance(getBalance(email))
                    .type(SERVICE_TYPE);
        } catch (Exception e) {
            builder.error(e.getMessage());
            System.err.println("TYPE-2: " + e.getMessage());
        }
        return builder.buildWithTimestamp();
    }

    public long getBalance(String email) throws Exception {
        Future<User> userFuture = multithreadedService.submit(() -> repository.getUser(email));
        User user = userFuture.get();
        return user.getBalance();
    }
}
