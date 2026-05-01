package com.StreetAlert.Street_Alert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StreetAlertApplication {
	public static void main(String[] args) {
		SpringApplication.run(StreetAlertApplication.class, args);
	}
}
