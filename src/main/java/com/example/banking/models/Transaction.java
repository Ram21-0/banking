package com.example.banking.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Transaction {
    String email;
    long value;

    @Override
    public String toString() {
        return String.format("Transaction [%s: %d]", email, value);
    }
}
