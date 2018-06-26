package com.grizzly.usermicro.user;

import com.grizzly.usermicro.admin.Admin;
import com.grizzly.usermicro.admin.AdminRepository;
import com.grizzly.usermicro.customer.Customer;
import com.grizzly.usermicro.customer.CustomerDTO;
import com.grizzly.usermicro.customer.CustomerRepository;
import com.grizzly.usermicro.vendor.Vendor;
import com.grizzly.usermicro.vendor.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

// TOASK: What dat v
@Service
public class UserService {
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AdminRepository adminRepository;

    public ArrayList<Customer> getAllCustomers(Integer pageIndex, String column_name) {
        PageRequest request = getPageRequest(pageIndex, column_name);
        return makeListFromIterable(customerRepository.findAll(request));
    }


    public ArrayList<Vendor> getAllVendors(Integer pageIndex, String column_name) {
        PageRequest request = getPageRequest(pageIndex, column_name);
        return makeListFromIterable(vendorRepository.findAll(request));
    }


    public ArrayList<Admin> getAllAdmins(Integer pageIndex, String column_name) {
        PageRequest request = getPageRequest(pageIndex, column_name);
        return makeListFromIterable(adminRepository.findAll(request));
    }

    // Lets find the user...
    // Start with admin -> vendor -> customer.
    public User findByUserEmail(String email) {
        // Check admin repo.
        User userFound = adminRepository.findByUserEmail(email);

        // Found!? Great. return it.
        if (userFound != null)
            return userFound;

        // Not found... check vendor
        userFound = vendorRepository.findByUserEmail(email);

        // We found it now? return it.
        if (userFound != null)
            return userFound;

        // eugh. still haven't found the user.
        userFound = customerRepository.findByUserEmail(email);

        // either we found it or not, return it.
        return userFound;
    }

    /**
     * Utility function to generate a pagerequest to tell the database how to page and sort a query
     * @param column_name, the fieldname in the database to sort the list
     * @return pageRequest to the method called
     */
    public PageRequest getPageRequest(Integer pageIndex, String column_name) {
        final String[] fields = {"userId", "name", "contact_num", "email"};
        String sortField;
        if (Arrays.asList(fields).contains(column_name)) {
            sortField = column_name;
        } else {
            sortField = "userId";
        }

        Sort sort = new Sort(Sort.Direction.ASC, sortField);

        PageRequest request = PageRequest.of(pageIndex, 25, sort);
        return request;
    }

    /**
     * Get a single item from customer id.
     * @param search, the string to match to ID to filter the user by
     * @return ArrayList of User objs whose names or IDs
     */
    public ArrayList<Customer> getSingleUserCustomer(Integer search) {
        Sort sort = new Sort(Sort.Direction.ASC, "userId");
        PageRequest request = PageRequest.of(0, 1, sort);

        return makeListFromIterable(
                customerRepository.findByUserCustomerId(search, request)
        );
    }

    /**
     * Get a single item from vendor id.
     * @param search, the string to match to ID to filter the user by
     * @return ArrayList of Vendor objs whose names or IDs
     */
    public ArrayList<Vendor> getSingleUserVendor(Integer search) {
        Sort sort = new Sort(Sort.Direction.ASC, "userId");
        PageRequest request = PageRequest.of(0, 1, sort);

        return makeListFromIterable(
                vendorRepository.findByUserVendorId(search, request)
        );
    }

    /**
     * Get a single item from user id.
     * @param search, the string to match to ID to filter the user by
     * @return ArrayList of User objs whose names or IDs
     */
    public ArrayList<Admin> getSingleUserAdmin(Integer search) {
        Sort sort = new Sort(Sort.Direction.ASC, "userId");
        PageRequest request = PageRequest.of(0, 1, sort);

        return makeListFromIterable(
                adminRepository.findByUserAdminId(search, request)
        );
    }

    public User addNewUser(CustomerDTO user) {
        User created = customerRepository.save(user.toEntity());

        return created;
    }

    /**
     * Make an ArrayList of Objects based on a passed-in Iterable
     * @param iter An Iterable of Objects
     * @return An ArrayList made from the Iterable
     */
    public static <T> ArrayList<T> makeListFromIterable(Iterable<T> iter) {
        ArrayList<T> list = new ArrayList<T>();

        for(T item: iter) {
            list.add(item);
        }

        return list;
    }

}