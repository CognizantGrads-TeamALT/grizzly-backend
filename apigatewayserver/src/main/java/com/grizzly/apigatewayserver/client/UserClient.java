package com.grizzly.apigatewayserver.client;

import com.grizzly.apigatewayserver.model.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@FeignClient(name = "usermicro", decode404 = true)
public interface UserClient {
    @GetMapping(value = "/getByEmail")
    Object findByUserEmail(@RequestParam("email") String email);

    @PutMapping(value = "/saveAPI")
    Object addOrUpdateUser(@RequestBody CustomerDTO customerDTO);
}