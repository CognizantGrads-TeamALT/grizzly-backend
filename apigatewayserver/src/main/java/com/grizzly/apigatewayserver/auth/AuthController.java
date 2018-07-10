package com.grizzly.apigatewayserver.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.grizzly.apigatewayserver.security.SecurityConstants.HEADER_STRING;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    // respond with logged in users info from userclient
    @GetMapping("/userData")
    public ResponseEntity getUser(@RequestHeader(HEADER_STRING) String token) {
        if (token == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        AuthSession authSession = authService.sessionStart(token);

        if (authSession == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(authSession.getUserData(), HttpStatus.OK);
    }

    // clear token from cache
    @PutMapping("/logout")
    public ResponseEntity logout(@RequestHeader(HEADER_STRING) String token) {
        Boolean deleted = authService.deleteSession(token);

        if (deleted)
            return new ResponseEntity<>(deleted, HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}