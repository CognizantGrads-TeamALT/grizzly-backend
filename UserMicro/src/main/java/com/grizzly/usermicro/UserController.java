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
     * Return a list of all Admins in the system
     * @return Admins in a list
     */
    @GetMapping("/get/vendor/{pageIndex}/{column_name}")
    public ResponseEntity<ArrayList<Vendor>> getAllVendors(@PathVariable(value="pageIndex") Integer pageIndex, @PathVariable(value="column_name") String column_name) {
        ArrayList<Vendor> products = userService.getAllVendors(pageIndex, column_name);

        // no products found
        if (products == null || products.isEmpty()) {
            return new ResponseEntity<>(products, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Return a list of all Admins in the system
     * @return Admins in a list
     */
    @GetMapping("/get/customer/{pageIndex}/{column_name}")
    public ResponseEntity<ArrayList<Customer>> getAllCustomers(@PathVariable(value="pageIndex") Integer pageIndex, @PathVariable(value="column_name") String column_name) {
        ArrayList<Customer> products = userService.getAllCustomers(pageIndex, column_name);

        // no products found
        if (products == null || products.isEmpty()) {
            return new ResponseEntity<>(products, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Return a list of all Admins in the system
     * @return Admins in a list
     */
    @GetMapping("/get/admin/{pageIndex}/{column_name}")
    public ResponseEntity<ArrayList<Admin>> getAllAdmins(@PathVariable(value="pageIndex") Integer pageIndex, @PathVariable(value="column_name") String column_name) {
        ArrayList<Admin> products = userService.getAllAdmins(pageIndex, column_name);

        // no products found
        if (products == null || products.isEmpty()) {
            return new ResponseEntity<>(products, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Return a single admin user based on id
     * @param id, user ID
     * @return the user
     */
    @GetMapping("/get/admin/{id}")
    public ResponseEntity<ArrayList<Admin>> getSingleUserAdmin(@PathVariable(value="id") String id) {
        ArrayList<Admin> users = userService.getSingleUserAdmin(id);

        // no users found
        if (users == null || users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Return a single vendor user based on id
     * @param id, user ID
     * @return the user
     */
    @GetMapping("/get/vendor/{id}")
    public ResponseEntity<ArrayList<Vendor>> getSingleUserVendor(@PathVariable(value="id") String id) {
        ArrayList<Vendor> users = userService.getSingleUserVendor(id);

        // no users found
        if (users == null || users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Return a single customer user based on id
     * @param id, user ID
     * @return the user
     */
    @GetMapping("/get/customer/{id}")
    public ResponseEntity<ArrayList<Customer>> getSingleUserCustomer(@PathVariable(value="id") String id) {
        ArrayList<Customer> users = userService.getSingleUserCustomer(id);

        // no users found
        if (users == null || users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}