package com.example.social_media_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories
public class SocialMediaAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialMediaAppApplication.class, args);
	}

}
