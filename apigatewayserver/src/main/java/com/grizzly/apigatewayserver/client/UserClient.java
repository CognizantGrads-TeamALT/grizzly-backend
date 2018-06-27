package com.grizzly.apigatewayserver.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@FeignClient(name = "usermicro", decode404 = true)
public interface UserClient {
    @GetMapping(value = "/get/{email}")
    Object findByUserEmail(@RequestParam("email") String email);

    @PostMapping(value = "/add")
    Object addNewUser(@RequestParam("user") UserDTO user);
}

