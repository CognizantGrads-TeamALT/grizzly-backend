package com.grizzly.apigatewayserver.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.grizzly.apigatewayserver.client.UserClient;
import com.grizzly.apigatewayserver.security.GoogleAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class AuthController {
    @Autowired
    private UserClient userClient;

    @Autowired
    private AuthService authService;

    // dev purposes. we can use this to check if a token is valid or not.
    @GetMapping("/get/{token}")
    public ResponseEntity get(@PathVariable(value = "token") String idTokenString) {
        //GoogleIdToken.Payload output = GoogleAuthenticator.verifyIdToken(idTokenString);
        AuthSession authSession = authService.sessionStart(idTokenString);

        //try {
            return new ResponseEntity<>(authSession, HttpStatus.OK);
        //} catch (IOException e) {
        //    return new ResponseEntity(HttpStatus.NOT_FOUND);
        //}
    }

    // dev purposes. we can use this to check if a user exists, and if so spits out their info
    // uses feignclient to connect to user micro.
    @GetMapping("/get/user/{email}")
    public ResponseEntity getUser(@PathVariable(value="email") String email) {
        Object user = userClient.findByUserEmail(email);

        if (user == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // maybe theres a better way to implement this.
    // we can't read authorization bearer through this IIRC.
    @GetMapping("/logout")
    public ResponseEntity logout() {
        return new ResponseEntity(HttpStatus.OK);
    }
}