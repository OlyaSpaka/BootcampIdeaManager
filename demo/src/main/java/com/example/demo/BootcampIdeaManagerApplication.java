package com.example.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BootcampIdeaManagerApplication {
	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("spring.datasource.url", dotenv.get("DB_URL"));
		System.setProperty("spring.datasource.username", dotenv.get("DB_USERNAME"));
		System.setProperty("spring.datasource.password", dotenv.get("DB_PASSWORD"));
		System.setProperty("azure.storage.connection-string", dotenv.get("PICTURE_STORAGE_CONN_STRING"));
		System.setProperty("azure.storage.container-name", dotenv.get("PICTURE_STORAGE_NAME"));
		SpringApplication.run(BootcampIdeaManagerApplication.class, args);
	}
}
