package com.example.banking;

import com.example.banking.models.Transaction;
import com.example.banking.models.User;
import com.example.banking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.LongStream;

@SpringBootTest
class BankingApplicationTests {

	@Autowired
	UserRepository repository;

	Random random = new Random();

	ExecutorService executorService = Executors.newCachedThreadPool();

	@Test
	void contextLoads() throws ExecutionException, InterruptedException {

		List<User> users = repository.getAllUsers();

		List<Transaction> transactions = new ArrayList<>();

//		LongStream.range(1,100)
//		random.longs(2000,-1000,1000)
//				.forEach(transactions::add);

		String email = "name1@gmail.com";
		Future<User> userFuture = executorService.submit(() -> repository.getUser(email));
		User user = userFuture.get();

		long sum = user.getBalance();
		for(var i : transactions) sum += i.getValue();

		performTransactions(transactions);

		System.out.println(sum);
	}

	public void performTransactions(List<Transaction> transactions) {
        transactions.forEach(transaction -> {
//			repository.updateBalance(email,value);
            executorService.submit(() -> repository.updateBalance(transaction));
        });
    }

}
