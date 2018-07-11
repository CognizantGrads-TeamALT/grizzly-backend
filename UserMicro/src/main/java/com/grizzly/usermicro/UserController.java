package com.grizzly.usermicro;

import java.util.ArrayList;

import com.grizzly.grizlibrary.errorhandling.ApiError;
import static com.grizzly.grizlibrary.helpers.Helper.buildResponse;

import com.grizzly.usermicro.admin.Admin;
import com.grizzly.usermicro.customer.Customer;
import com.grizzly.usermicro.customer.CustomerDTO;
import com.grizzly.usermicro.orders.OrderDTO;
import com.grizzly.usermicro.user.User;
import com.grizzly.usermicro.vendor.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
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
     * Returns "accessLevel"
     * @param userData
     * @return Integer
     *
     * true = admin
     * false = not admin
     */
    private Boolean hasAccess(String userData) {
        try {
            JSONObject jsonObject = new JSONObject(userData);
            return jsonObject.get("role").equals("admin");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Return a list of all Admins in the system
     * @return Admins in a list
     */
    @GetMapping("/get/vendor/{pageIndex}/{column_name}")
    public ResponseEntity getAllVendors(@PathVariable(value="pageIndex") Integer pageIndex, @PathVariable(value="column_name") String column_name, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

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
    public ResponseEntity getAllCustomers(@PathVariable(value="pageIndex") Integer pageIndex, @PathVariable(value="column_name") String column_name, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

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
    public ResponseEntity getAllAdmins(@PathVariable(value="pageIndex") Integer pageIndex, @PathVariable(value="column_name") String column_name, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

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
    public ResponseEntity getSingleUserAdmin(@PathVariable(value="id") Integer id, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

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
    public ResponseEntity getSingleUserVendor(@PathVariable(value="id") Integer id, @RequestHeader(value="User-Data") String userData) {if (!hasAccess(userData))
        return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

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
    public ResponseEntity getSingleUserCustomer(@PathVariable(value="id") Integer id, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

        ArrayList<Customer> customers = userService.getSingleUserCustomer(id);

        // no users found
        if (customers == null || customers.isEmpty()) {
            return new ResponseEntity<>(customers, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    /**
     * Return a single customer with orders based on their id
     * @return A CustomerDTO with orders
     */
    @GetMapping("/get/orders")
    public ResponseEntity getSingleWithOrders(@RequestHeader(value="User-Data") String userData) {
        Integer userId;
        try {
            JSONObject jsonObject = new JSONObject(userData);
            userId = (Integer) jsonObject.get("userId");
            if (!jsonObject.get("role").equals("customer"))
                return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));
        } catch (Exception e) {
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));
        }

        CustomerDTO customer = userService.getSingleCustomerWithOrders(userId);

        // no product found
        if (customer == null) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "No user was found.", "userId: " + userId));
        }

        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    /**
     * Return a single user based on email
     * @param email, user email
     * @return the user
     *
     * restricted at apigateway so feignclient accesses this.
     */
    @GetMapping("/getByEmail/{email}")
    public ResponseEntity getSingleUser(@PathVariable(value="email") String email) {
        User user = userService.findByUserEmail(email);

        if (user == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Adds and or updates a customer account (restricted to apigateway)
     * @param userDTO data
     * @return user
     */
    @PutMapping("/saveAPI")
    public ResponseEntity addOrUpdateCustomer(@RequestBody CustomerDTO userDTO) {
        User user = userService.addOrUpdateUser(userDTO);

        if (user == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Adds and or updates a customer account.
     * @param
     * @return user
     *
     * do not remove user-data check.
     */
    @GetMapping("/userData")
    public ResponseEntity getData(@RequestHeader(value="User-Data") String userData) {
        User user;

        try {
            JSONObject jsonObject = new JSONObject(userData);
            user = userService.findByUserEmail(jsonObject.get("email").toString());
        } catch (Exception e) {
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));
        }

        if (user == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Adds and or updates a customer account.
     * @param userDTO data
     * @return user
     *
     * do not remove user-data check.
     */
    @PutMapping("/save")
    public ResponseEntity addOrUpdateCustomer(@RequestBody CustomerDTO userDTO, @RequestHeader(value="User-Data") String userData) {
        User user = userService.addOrUpdateUser(userDTO);

        if (user == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Add a new order based on a given DTO resource
     * @param orderDTO, the new order to store in the database
     * @return Http Status Message
     */
    @PutMapping("/addOrder")
    public ResponseEntity addOrder(@RequestBody OrderDTO orderDTO, @RequestHeader(value="User-Data") String userData) {
        Integer userId;
        try {
            JSONObject jsonObject = new JSONObject(userData);
            userId = (Integer) jsonObject.get("userId");
            if (!jsonObject.get("role").equals("customer"))
                return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

            if (orderDTO.getUser_id() != userId)
                return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));
        } catch (Exception e) {
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));
        }

        userService.addOrder(orderDTO);

        return new ResponseEntity<>(orderDTO.getTxn_id(), HttpStatus.OK);
    }
}


