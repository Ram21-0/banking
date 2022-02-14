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



	@Test
	void contextLoads() {

		User user = User.builder().name("Ram").build();
		System.out.println(user);
	}

}
