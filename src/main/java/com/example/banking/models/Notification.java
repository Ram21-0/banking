package com.example.banking.models;

import com.example.banking.services.NotifierService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification implements Comparable<Notification> {
    private String email;
    private long balance;
    private Date timestamp;
    private String errorMessage;
    private String type;

    private Notification(String email) {
        this.email = email;
        this.type = "GENERAL";
    }

    public static class NotificationBuilder {

        private final Notification notification;

        private NotificationBuilder() {
            this.notification = new Notification();
        }

        public NotificationBuilder(String email) {
            this.notification = new Notification(email);
        }

        public NotificationBuilder error(String errorMessage) {
            this.notification.errorMessage = errorMessage;
            return this;
        }

        public NotificationBuilder timestamp(Date timestamp) {
            this.notification.timestamp = timestamp;
            return this;
        }

        public NotificationBuilder balance(long balance) {
            this.notification.balance = balance;
            return this;
        }

        public NotificationBuilder email(String email) {
            this.notification.email = email;
            return this;
        }

        public NotificationBuilder type(String type) {
            this.notification.type = type;
            return this;
        }

        public Notification build() {
            return this.notification;
        }

        public Notification buildWithTimestamp() {
            return this.timestamp(new Date()).notification;
        }
    }

    @Override
    public int compareTo(Notification notif) {
        return - this.timestamp.compareTo(notif.timestamp);
    }

    @Override
    public String toString() {
        return String.format("[%s has ₹%d : %s] %s",email,balance,timestamp,type);
    }
}
