package com.grizzly.usermicro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

// TOASK: What dat v
@Service
public class UserService {
    @Autowired
    private VendorRepository vendorRepository;
    private CustomerRepository customerRepository;
    private AdminRepository adminRepository;

    /**
     * Get a single item from customer id.
     * @param search, the string to match to ID to filter the user by
     * @return ArrayList of User objs whose names or IDs
     */
    public ArrayList<Customer> getSingleUserCustomer(String search) {
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
    public ArrayList<Vendor> getSingleUserVendor(String search) {
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
    public ArrayList<Admin> getSingleUserAdmin(String search) {
        Sort sort = new Sort(Sort.Direction.ASC, "userId");
        PageRequest request = PageRequest.of(0, 1, sort);

        return makeListFromIterable(
                adminRepository.findByUserAdminId(search, request)
        );
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