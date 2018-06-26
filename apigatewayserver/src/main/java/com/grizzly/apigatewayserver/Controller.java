package com.grizzly.apigatewayserver;

import com.grizzly.apigatewayserver.security.GoogleAuthenticator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class Controller {
    @GetMapping("/get/{token}")
    public ResponseEntity get(@PathVariable(value = "token") String idTokenString) {
        Boolean output = GoogleAuthenticator.verifyIdToken(idTokenString);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}