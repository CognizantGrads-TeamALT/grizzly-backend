package com.grizzly.apigatewayserver.client;

import com.grizzly.apigatewayserver.model.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(name = "usermicro", decode404 = true)
public interface UserClient {
    @GetMapping(value = "/getByEmail/{email}")
    Object findByUserEmail(@PathVariable("email") String email);

    @PutMapping(value = "/saveAPI")
    Object addOrUpdateUser(@RequestBody CustomerDTO customerDTO);
}