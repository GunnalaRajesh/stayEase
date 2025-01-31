package com.application.StayEase;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication
@EntityScan(basePackages = "com.application.StayEase.entity")
public class StayEaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(StayEaseApplication.class, args);
	}

}
