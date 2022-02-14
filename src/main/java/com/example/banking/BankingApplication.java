package com.example.banking;

import com.example.banking.services.NotifierService;
import com.example.banking.services.Service1;
import com.example.banking.services.Service2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankingApplication implements CommandLineRunner {

	@Autowired
	private NotifierService notifier;

//	@Autowired
//	private Service1 s1;
//
//	@Autowired
//	private Service2 s2;

	@Autowired
	private final Service1[] services = new Service1[10];

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}

	@Override
	public void run(String... args) {
		notifier.run();
//		s1.run();
//		s2.run();
		for(var service : services) service.run();
	}
}
