package com.example.banking.models;

public class Queries {

    private static final String TABLE_NAME = "users";

    public static String getAllUsersQuery() {
        return String.format("select * from %s;", TABLE_NAME);
    }

    public static String updateBalanceQuery(String email,long value) {
        return String.format("update %s set balance = balance + %d where email = \"%s\";",
                TABLE_NAME, value, email);
    }

    public static String getUserByEmailQuery(String email) {
        return String.format("select * from %s where email = \"%s\";",
                TABLE_NAME, email);
    }

    public static String getUserCountQuery() {
        return String.format("select count(*) from %s;", TABLE_NAME);
    }

    public static String updateNameQuery(String email,String newName) {
        return String.format("update %s set name = \"%s\" where email = \"%s\";",
                TABLE_NAME, newName, email);

    }

}
