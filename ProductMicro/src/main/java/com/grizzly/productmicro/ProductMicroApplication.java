package com.grizzly.productmicro;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class ProductMicroApplication {
    @Value("${mysql.username}")
    private String username;

    @Value("${mysql.password}")
    private String password;

    public static void main(String[] args) {
        SpringApplication.run(ProductMicroApplication.class, args);
    }
}