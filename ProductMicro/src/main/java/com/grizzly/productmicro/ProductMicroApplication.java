package com.grizzly.productmicro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProductMicroApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductMicroApplication.class, args);
    }
}