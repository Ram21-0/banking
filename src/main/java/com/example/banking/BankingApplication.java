package com.example.banking;

import com.example.banking.executors.MultithreadedService;
import com.example.banking.executors.ScheduledService;
import com.example.banking.models.Notification;
import com.example.banking.models.Transaction;
import com.example.banking.models.User;
import com.example.banking.notif.Notifier;
import com.example.banking.repository.UserRepository;
import com.example.banking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class BankingApplication implements CommandLineRunner {

	@Autowired
	private UserRepository repository;

	@Autowired
	private MultithreadedService multithreadedService;

	@Autowired
	private ScheduledService scheduledService;

	private final ConcurrentHashMap<String,Long> balances = new ConcurrentHashMap<>();

	@Autowired
	private Notifier<Notification> notifier;

	private List<String> emails;

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Future<List<User>> users = multithreadedService.submit(repository::getAllUsers);
		emails = users.get().stream().map(User::getEmail).collect(Collectors.toList());

		emails.forEach(email -> {
			try {
				balances.put(email, getBalance(email));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		notifier.run();

		for(String email : emails) {
			scheduledService.scheduleRandomly(() -> performTransaction(createTransaction(email)),30);
		}

//		scheduledService.schedule(
//				() -> performTransaction(createTransaction(email1)),
//				7, TimeUnit.SECONDS
//		);
//
//		scheduledService.schedule(
//				() -> performTransaction(createTransaction(email2)),
//				6,TimeUnit.SECONDS
//		);
	}

	public Transaction createTransaction(String email) {
		long balance = Utils.getRandomTransactionAmount(2000);
		return new Transaction(email,balance);
	}

	private String getRandomEmail() {
		int index = Utils.randomInt(emails.size());
		return emails.get(index);
	}

	public Notification performUpdate(Transaction transaction) {

		Notification.Builder builder = new Notification.Builder(transaction.getEmail());
		try {
			System.out.println(transaction);
			balances.put(transaction.getEmail(),balances.get(transaction.getEmail()) + transaction.getValue());
//			printExpectedBalance();
			repository.updateBalance(transaction);
			builder.balance(getBalance(transaction.getEmail()));
		} catch (Exception e) {
			builder.error(e.getMessage());
		}
		return builder.buildWithTimestamp();
	}

	public void printExpectedBalance() {
		System.out.println(
				"--------- EXPECTED ---------\n"
				+ balances
				+ "\n----------------------------"
		);
	}

	public void performTransaction(Transaction transaction) {
		CompletableFuture.supplyAsync(() -> performUpdate(transaction))
				.thenAccept(notifier::add);
	}

	public long getBalance(String email) throws ExecutionException, InterruptedException {
		Future<User> userFuture = multithreadedService.submit(() -> repository.getUser(email));
		User user = userFuture.get();
		return user.getBalance();
	}
}
