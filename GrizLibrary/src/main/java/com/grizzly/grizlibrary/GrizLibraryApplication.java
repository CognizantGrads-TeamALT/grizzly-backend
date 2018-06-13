package com.grizzly.grizlibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GrizLibraryApplication {
    public static void main(String[] args) {
        SpringApplication.run(GrizLibraryApplication.class, args);
    }
}