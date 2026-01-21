package com.escueladeequitacion.hrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HrsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HrsApplication.class, args);
	}

}
