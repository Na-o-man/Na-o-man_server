package com.umc.naoman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableJpaAuditing
public class NaomanApplication {

	public static void main(String[] args) {
		SpringApplication.run(NaomanApplication.class, args);
	}

}
