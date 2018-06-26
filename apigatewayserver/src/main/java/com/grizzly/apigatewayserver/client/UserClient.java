package com.grizzly.apigatewayserver.client;

import com.grizzly.apigatewayserver.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@FeignClient(name = "usermicro", decode404 = true)
public interface UserClient {
    @GetMapping(value = "/get/{email}")
    User findByUserEmail(@RequestParam("email") String email);

    @PostMapping(value = "/add")
    User addNewUser(@RequestParam("user") User user);
}
