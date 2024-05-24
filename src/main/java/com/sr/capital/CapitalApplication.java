package com.sr.capital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.omunify.*","com.sr.capital.*"})
public class CapitalApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapitalApplication.class, args);
	}

}
