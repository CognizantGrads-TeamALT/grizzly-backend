package com.grizzly.categorymicro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CategoryMicroApplication {
	public static void main(String[] args) {
		SpringApplication.run(CategoryMicroApplication.class, args);
	}
}