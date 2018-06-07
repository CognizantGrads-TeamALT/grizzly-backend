package com.grizzly.usermicro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserMicroApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserMicroApplication.class, args);
    }
}
