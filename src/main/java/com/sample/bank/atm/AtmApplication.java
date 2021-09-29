package com.sample.bank.atm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableJpaRepositories
@EnableRetry
public class AtmApplication {
	public static void main(String[] args) {
		SpringApplication.run(AtmApplication.class, args);
	}
	
}