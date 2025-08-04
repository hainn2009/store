package com.example.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(StoreApplication.class, args);
		// ConfigurableApplicationContext context =
		// SpringApplication.run(StoreApplication.class, args);
		// System.out.println("🔍 Active Profiles: " +
		// Arrays.toString(context.getEnvironment().getActiveProfiles()));
	}
}
