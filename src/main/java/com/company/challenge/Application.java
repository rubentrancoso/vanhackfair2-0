package com.company.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.company.challenge.repositories.UserRepository;

@SpringBootApplication
@EnableAutoConfiguration
@EnableJpaRepositories(basePackageClasses = { UserRepository.class })
@EntityScan(basePackages = { "com.company.challenge.entities" })
@ComponentScan(basePackages = { 
		"com.company.challenge.config", 
		"com.company.challenge.audit",
		"com.company.challenge.services", 
		"com.company.challenge.userapi.endpoints", 
		"com.company.challenge.security" 
	}
)
public class Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
