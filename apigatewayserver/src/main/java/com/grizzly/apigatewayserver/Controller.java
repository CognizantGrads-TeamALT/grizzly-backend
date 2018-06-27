package com.grizzly.apigatewayserver;

import com.grizzly.apigatewayserver.client.UserClient;
import com.grizzly.apigatewayserver.security.GoogleAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class Controller {
    // dev purposes. we can use this to check if a token is valid or not.
    @GetMapping("/get/{token}")
    public ResponseEntity get(@PathVariable(value = "token") String idTokenString) {
        Boolean output = GoogleAuthenticator.verifyIdToken(idTokenString);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @Autowired
    private UserClient userClient;

    // dev purposes. we can use this to check if a user exists, and if so spits out their info
    // uses feignclient to connect to user micro.
    @GetMapping("/get/user/{email}")
    public ResponseEntity<Object> getUser(@PathVariable(value="email") String email) {
        Object user = userClient.findByUserEmail(email);

        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}