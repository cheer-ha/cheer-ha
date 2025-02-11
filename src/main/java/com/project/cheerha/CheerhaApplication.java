package com.project.cheerha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CheerhaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CheerhaApplication.class, args);
	}

}
