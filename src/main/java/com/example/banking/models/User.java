package com.example.banking.models;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class User {
    private String email;
    private String name;
    private long balance;
}
