package com.example.banking.models;

import lombok.Getter;

import java.util.Date;

@Getter
public class Notification implements Comparable<Notification> {
    private String email;
    private long balance;
    private Date timestamp;
    private String errorMessage;

    private Notification(String email) {
        this.email = email;
    }

    public static class Builder {

        private final Notification notification;

        public Builder(String email) {
            this.notification = new Notification(email);
        }

        public Builder error(String errorMessage) {
            this.notification.errorMessage = errorMessage;
            return this;
        }

        public Builder timestamp(Date timestamp) {
            this.notification.timestamp = timestamp;
            return this;
        }

        public Builder balance(long balance) {
            this.notification.balance = balance;
            return this;
        }

        public Builder email(String email) {
            this.notification.email = email;
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
        return String.format("[%s has ₹%d : %s]",email,balance,timestamp);
    }
}
