package com.example.banking.services;

import com.example.banking.executors.MultithreadedService;
import com.example.banking.executors.ScheduledService;
import com.example.banking.models.Notification;
import com.example.banking.models.Transaction;
import com.example.banking.models.User;
import com.example.banking.repository.UserRepository;
import com.example.banking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class Service1 {

    @Autowired
    private ScheduledService scheduledService;

    @Autowired
    private MultithreadedService multithreadedService;

    @Autowired
    private UserRepository repository;

    @Autowired
    private NotifierService notifier;

    private final String SERVICE_TYPE = "TYPE-1";

    public void run() {

        for (String email : Utils.emails) {
            scheduledService.scheduleRandomly(
                    () -> performTransaction(createTransaction(email)),
                    60
            );
        }
    }

    public Transaction createTransaction(String email) {
        long balance = Utils.getRandomTransactionAmount(2000);
        return new Transaction(email,balance);
    }

    public Notification performUpdate(Transaction transaction) {

        Notification.NotificationBuilder builder = new Notification.NotificationBuilder(transaction.getEmail());
        try {
//            System.out.println(transaction);
            repository.updateBalance(transaction);
            builder.balance(getBalance(transaction.getEmail()));
        } catch (Exception e) {
            builder.error(e.getMessage());
            System.err.println("TYPE-1: " + e.getMessage());
        }
        return builder.type(SERVICE_TYPE).buildWithTimestamp();
    }

    public void performTransaction(Transaction transaction) {
        CompletableFuture.supplyAsync(() -> performUpdate(transaction))
                .thenAccept(notifier::add);
    }

    public long getBalance(String email) throws Exception {
        Future<User> userFuture = multithreadedService.submit(() -> repository.getUser(email));
        User user = userFuture.get();
        return user.getBalance();
    }

}
