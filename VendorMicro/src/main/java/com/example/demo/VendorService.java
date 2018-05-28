package com.example.demo;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    /**
     * Get all vendors in the database
     * @return ArrayList of Vendor objs
     */
    public ArrayList<Vendor> getAll() {
        PageRequest request = PageRequest.of(0, 25);

        return makeListFromIterable(
                vendorRepository.findAll(request)
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
