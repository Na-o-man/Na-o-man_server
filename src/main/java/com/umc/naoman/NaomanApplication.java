package com.umc.naoman;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.ZonedDateTime;
import java.util.TimeZone;

@EnableJpaAuditing
@SpringBootApplication
@Slf4j
public class NaomanApplication {
	public static void main(String[] args) {
		SpringApplication.run(NaomanApplication.class, args);
	}

	@PostConstruct
	public void setTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		log.debug("타임존 설정 후 현재 시각: {}", ZonedDateTime.now());
	}
}
