package com.londonhydro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.londonhydro")
public class LondonHydroApplication {

	public static void main(String[] args) {
		SpringApplication.run(LondonHydroApplication.class, args);
	}

}
