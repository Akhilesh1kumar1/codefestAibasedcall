package com.sr.capital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"com.omunify.*","com.sr.capital.*"})
@EnableMongoRepositories(basePackages = {"com.sr.capital"})
@EnableAutoConfiguration
@EnableScheduling
@EnableAsync
public class CapitalApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapitalApplication.class, args);
	}

}
