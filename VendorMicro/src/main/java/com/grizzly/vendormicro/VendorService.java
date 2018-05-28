package com.grizzly.vendormicro;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    /**
     * Get all vendors in the database, sorted by name and paginated to 25 vendors at a time.
     * Currently only returns 1st page (can update with first param of PageRequest.of()
     * @return ArrayList of Vendor objs
     */
    public ArrayList<Vendor> getAll() {
        Sort sort = new Sort(Sort.Direction.ASC, "name");
        PageRequest request = PageRequest.of(0, 25, sort);

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
