package com.grizzly.apigatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableZuulProxy

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class ApigatewayserverApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApigatewayserverApplication.class, args);
    }

    @GetMapping("/")
    public ResponseEntity index() {
        return new ResponseEntity<>("YOU GET A COOKIE!", HttpStatus.OK);
    }
}
