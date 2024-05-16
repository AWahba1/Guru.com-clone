package com.guru.freelancerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class FreelancerMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FreelancerMsApplication.class, args);
	}

}
