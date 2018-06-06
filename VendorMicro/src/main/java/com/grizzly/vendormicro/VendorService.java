package com.grizzly.vendormicro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VendorService {
    @Autowired
    private VendorRepository vendorRepository;

    public ArrayList<Vendor> get(Integer pageIndex, String column_name) {
        PageRequest request = getPageRequest(pageIndex, column_name);
        return makeListFromIterable(vendorRepository.findAll(request));
    }

    /**
     * Utility function to generate a pagerequest to tell the database how to page and sort a query
     * @param column_name, the fieldname in the database to sort the list
     * @return pageRequest to the method called
     */
    public PageRequest getPageRequest(Integer pageIndex, String column_name) {
        final String[] fields = {"vendorId", "name", "contactNum", "website", "email", "bio"};
        String sortField;
        if (Arrays.asList(fields).contains(column_name)) {
            sortField = column_name;
        } else {
            sortField = "vendorId";
        }

        Sort sort = new Sort(Sort.Direction.ASC, sortField);

        PageRequest request = PageRequest.of(pageIndex, 25, sort);
        return request;
    }

    /**
     * Get a single item from product id.
     * @param id, the string to match to ID to filter the product by
     * @return ArrayList of Product objs whose names or IDs
     */
    public ArrayList<Vendor> getSingle(Integer id) {
        Sort sort = new Sort(Sort.Direction.ASC, "vendorId");
        PageRequest request = PageRequest.of(0, 25, sort);

        return makeListFromIterable(
                vendorRepository.findByVendorId(id, request)
        );
    }

    /**
     * Get a filtered list of vendors, based on a given search string to match to name or ID.
     * @param search, the string to match to name or ID to filter the vendors by
     * @return ArrayList of Vendor objs whose names or IDs
     */
    public ArrayList<Vendor> getFiltered(String search) {
        try {
            Integer vendorId = Integer.parseInt(search);

            return getSingle(vendorId);
        } catch(NumberFormatException e) {
            Sort sort = new Sort(Sort.Direction.ASC, "vendorId");
            PageRequest request = PageRequest.of(0, 25, sort);

            return makeListFromIterable(
                    vendorRepository.findByVendorName(search, request)
            );
        }
    }

    /**
     * Update an existing vendor (based on a given ID)
     * @param id, ID of the vendor to update
     * @param newBool, new status enabled/disabled of vendor
     */
    public Vendor setEnabled(Integer id, Boolean newBool) {
        // find the existing vendor
        Vendor vendor;
        try {
            vendor = vendorRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            return null;
        }

        // make changes
        vendor.setEnabled(newBool);

        // save the updated category
        vendorRepository.save(vendor);
        return vendor;
    }

    /**
     * Delete a vendor given an ID
     * @param deleteId, ID of the vendor to delete
     */
    public void deleteById(Integer deleteId) {
        vendorRepository.deleteById(deleteId);
    }

    /**
     * Add a new vendor to the database
     * @param newVendor, the entity of the new vendor to save
     * @return the added vendor object
     */
    public Vendor add(Vendor newVendor) {
        Vendor created = vendorRepository.save(newVendor);
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

    /**
     * Get a list of vendors based on vendor IDs
     * @param ids, The list of Vendor ids that are to be fetched
     * @return ArrayList of Vendor objs whose IDs match ids
     */
    public ArrayList<Vendor> getBatchbyId(List<String> ids) {
        List<Integer> ints = new ArrayList<Integer>();

        for(String id: ids){
            ints.add(Integer.parseInt(id));
        }
        return makeListFromIterable(vendorRepository.findByVendorIdIn(ints));
    }
}
