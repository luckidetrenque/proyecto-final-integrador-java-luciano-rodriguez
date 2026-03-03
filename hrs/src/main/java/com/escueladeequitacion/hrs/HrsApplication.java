package com.escueladeequitacion.hrs;

import java.util.TimeZone;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HrsApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		setIfPresent(dotenv, "SPRING_PROFILE");
		setIfPresent(dotenv, "DB_HOST");
		setIfPresent(dotenv, "DB_NAME");
		setIfPresent(dotenv, "DB_USER");
		setIfPresent(dotenv, "DB_PASSWORD");
		setIfPresent(dotenv, "DB_NAME_NEON");
		setIfPresent(dotenv, "DB_USER_NEON");
		setIfPresent(dotenv, "DB_PASSWORD_NEON");
		setIfPresent(dotenv, "WHITELIST_EMAILS");
		setIfPresent(dotenv, "CORS_ALLOWED_ORIGINS");
		setIfPresent(dotenv, "SERVER_PORT");

		TimeZone.setDefault(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));
		SpringApplication.run(HrsApplication.class, args);
	}

	private static void setIfPresent(Dotenv dotenv, String key) {
		String value = dotenv.get(key, null);
		if (value != null) {
			System.setProperty(key, value);
		}
	}
}