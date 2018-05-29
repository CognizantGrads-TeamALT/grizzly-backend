package com.grizzly.vendormicro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class VendormicroApplication {

    public static void main(String[] args) {
        SpringApplication.run(VendormicroApplication.class, args);
    }
}
