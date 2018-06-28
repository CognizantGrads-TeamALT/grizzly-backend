package com.grizzly.usermicro;

import java.util.ArrayList;

import com.grizzly.grizlibrary.errorhandling.ApiError;
import static com.grizzly.grizlibrary.helpers.Helper.buildResponse;

import com.grizzly.usermicro.admin.Admin;
import com.grizzly.usermicro.customer.Customer;
import com.grizzly.usermicro.customer.CustomerDTO;
import com.grizzly.usermicro.orders.OrderDTO;
import com.grizzly.usermicro.user.UserService;
import com.grizzly.usermicro.vendor.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Return a list of all Admins in the system
     * @return Admins in a list
     */
    @GetMapping("/get/vendor/{pageIndex}/{column_name}")
    public ResponseEntity<ArrayList<Vendor>> getAllVendors(@PathVariable(value="pageIndex") Integer pageIndex, @PathVariable(value="column_name") String column_name) {
        ArrayList<Vendor> vendors = userService.getAllVendors(pageIndex, column_name);

        // no products found
        if (vendors == null || vendors.isEmpty()) {
            return new ResponseEntity<>(vendors, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(vendors, HttpStatus.OK);
    }

    /**
     * Return a list of all Admins in the system
     * @return Admins in a list
     */
    @GetMapping("/get/customer/{pageIndex}/{column_name}")
    public ResponseEntity<ArrayList<Customer>> getAllCustomers(@PathVariable(value="pageIndex") Integer pageIndex, @PathVariable(value="column_name") String column_name) {
        ArrayList<Customer> customers = userService.getAllCustomers(pageIndex, column_name);

        // no products found
        if (customers == null || customers.isEmpty()) {
            return new ResponseEntity<>(customers, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    /**
     * Return a list of all Admins in the system
     * @return Admins in a list
     */
    @GetMapping("/get/admin/{pageIndex}/{column_name}")
    public ResponseEntity<ArrayList<Admin>> getAllAdmins(@PathVariable(value="pageIndex") Integer pageIndex, @PathVariable(value="column_name") String column_name) {
        ArrayList<Admin> admins = userService.getAllAdmins(pageIndex, column_name);

        // no products found
        if (admins == null || admins.isEmpty()) {
            return new ResponseEntity<>(admins, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(admins, HttpStatus.OK);
    }

    /**
     * Return a single admin user based on id
     * @param id, user ID
     * @return the user
     */
    @GetMapping("/get/admin/{id}")
    public ResponseEntity<ArrayList<Admin>> getSingleUserAdmin(@PathVariable(value="id") Integer id) {
        ArrayList<Admin> admins = userService.getSingleUserAdmin(id);

        // no users found
        if (admins == null || admins.isEmpty()) {
            return new ResponseEntity<>(admins, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(admins, HttpStatus.OK);
    }

    /**
     * Return a single vendor user based on id
     * @param id, user ID
     * @return the user
     */
    @GetMapping("/get/vendor/{id}")
    public ResponseEntity<ArrayList<Vendor>> getSingleUserVendor(@PathVariable(value="id") Integer id) {
        ArrayList<Vendor> vendors = userService.getSingleUserVendor(id);

        // no users found
        if (vendors == null || vendors.isEmpty()) {
            return new ResponseEntity<>(vendors, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(vendors, HttpStatus.OK);
    }

    /**
     * Return a single customer user based on id
     * @param id, user ID
     * @return the user
     */
    @GetMapping("/get/customer/{id}")
    public ResponseEntity<ArrayList<Customer>> getSingleUserCustomer(@PathVariable(value="id") Integer id) {
        ArrayList<Customer> customers = userService.getSingleUserCustomer(id);

        // no users found
        if (customers == null || customers.isEmpty()) {
            return new ResponseEntity<>(customers, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    /**
     * Return a single customer with orders based on id
     * @param id, user ID
     * @return the customer user with orders
     */
    @GetMapping("/get/orders/{id}")
    public ResponseEntity getSingleWithOrders(@PathVariable(value="id") Integer id) {
        CustomerDTO customer = userService.getSingleWithOrders(id);

        // no product found
        if (customer == null) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "No user was found.", "id: " + id));
        }

        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    /**
     * Return a single order from a customer
     * @param id, user ID, orderId
     * @return the customer with order
     */
    @GetMapping("/getOrder/{id}/{orderId}")
    public ResponseEntity getSingleOrder(@PathVariable(value="id") Integer id, @PathVariable(value="orderId") Integer orderId) {
        OrderDTO order = userService.getOrderFromCustomer(id, orderId);

        if (order == null) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "No image was found.", "id: " + id + " " + "orderId: " + orderId));
        }

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

}