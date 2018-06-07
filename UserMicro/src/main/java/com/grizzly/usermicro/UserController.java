package com.grizzly.usermicro;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Return a single user based on id
     * @param id, user ID
     * @return the user
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<ArrayList<User>> getSingle(@PathVariable(value="id") String id) {
        ArrayList<User> users = userService.getSingle(id);

        // no users found
        if (users == null || users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}