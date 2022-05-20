package com.github.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class TestingWebApplication {

	public static void main(String[] args) {
		// modified this if we need to change port
		SpringApplication app = new SpringApplication(TestingWebApplication.class);
		app.setDefaultProperties(Collections
				.singletonMap("server.port", "8083"));
		app.run(args);
	}
}
