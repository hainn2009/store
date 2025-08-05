package com.example.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class StoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(StoreApplication.class, args);
		// ConfigurableApplicationContext context =
		// SpringApplication.run(StoreApplication.class, args);
		// System.out.println("🔍 Active Profiles: " +
		// Arrays.toString(context.getEnvironment().getActiveProfiles()));
	}

	@PostConstruct
	public void printActiveProfile() {
		String profile = System.getenv("SPRING_PROFILES_ACTIVE");
		System.out.println(">>>>> SPRING_PROFILES_ACTIVE = " + profile);
	}
}
