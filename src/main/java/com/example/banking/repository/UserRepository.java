package com.example.banking.repository;

import com.example.banking.models.Queries;
import com.example.banking.models.Transaction;
import com.example.banking.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void updateBalance(Transaction transaction) {
        String query = Queries.updateBalanceQuery(transaction.getEmail(), transaction.getValue());
        jdbcTemplate.update(query);
    }

    public List<User> getAllUsers() {
        String query = Queries.getAllUsersQuery();
        return jdbcTemplate.query(
                query,
                new BeanPropertyRowMapper<>(User.class)
        );
    }

    public User getUser(String email) {
        String query = Queries.getUserByEmailQuery(email);
        return jdbcTemplate.queryForObject(
                query,
                new BeanPropertyRowMapper<>(User.class));
    }

    private String getQueryObject(int index) {

        return "("
                + "\"name" + index + "@gmail.com\", "
                + "\"name" + index + "\", "
                + "10000"
                + ")";
    }

    public List<User> generateUsers(int start, int end) {
        String prefix = "insert into users values ";
        String query = IntStream.rangeClosed(start,end)
                .mapToObj(this::getQueryObject)
                .collect(Collectors.joining(",",prefix,";"));

        System.out.println(query);

        jdbcTemplate.update(query);
        return new ArrayList<>();
    }

    public int getUserCount() {
        String query = Queries.getUserCountQuery();
        OptionalInt count = jdbcTemplate.queryForObject(query, OptionalInt.class);
        return count != null && count.isPresent()
                ? count.getAsInt()
                : 0;
    }

    public void updateName(String email,String newName) {
        String query = Queries.updateNameQuery(email,newName);
        jdbcTemplate.execute(query);
    }
}
