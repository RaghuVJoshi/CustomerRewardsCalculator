package com.raghuvjoshi.customerrewardsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class CustomerRewardsServiceApplication {

	public static void main(String[] args) {
			SpringApplication.run(CustomerRewardsServiceApplication.class, args);
	}

}
