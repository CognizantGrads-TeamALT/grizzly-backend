package com.grizzly.productmicro.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(name = "categorymicro", decode404 = true)
public interface CategoryClient {
    @PostMapping(value="/updateCount")
    Object updateCount(@RequestParam("catID") Integer catID, @RequestParam("newCount") Integer newCount);
}