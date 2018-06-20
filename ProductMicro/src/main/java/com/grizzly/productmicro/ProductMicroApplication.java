package com.grizzly.productmicro;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(
            value = "/test",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public String test() {
        return username + " " + password;
    }
}