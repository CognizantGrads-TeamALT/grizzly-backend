package com.grizzly.vendormicro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;


@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;


    public ArrayList<Vendor> getAll(String column_name)
    {

       PageRequest request = getPageRequest(column_name);
       return makeListFromIterable(vendorRepository.findAll(request));


  }
    /**
     * Utility function to generate a pagerequest to tell the database how to page and sort a query
     * @param column_name, the fieldname in the database to sort the list
     * @return pageRequest to the method called
    */
  public PageRequest getPageRequest(String column_name)
  {
      Sort sort;


      final String[] fields = {"vendorId", "name", "contactNum", "website", "email", "bio"};
      String sortField;
      if (Arrays.asList(fields).contains(column_name)) {
          sortField = column_name;
      } else {
          sortField = "vendorId";

      }
      sort = new Sort(Sort.Direction.ASC, sortField);

      PageRequest request = PageRequest.of(0, 25, sort);
      return request;
  }
    /**
     * Get a filtered list of vendors, based on a given search string to match to name or ID.
     * @param search, the string to match to name or ID to filter the vendors by
     * @return ArrayList of Vendor objs whose names or IDs
     */
    public ArrayList<Vendor> getFiltered(String search) {
        Sort sort = new Sort(Sort.Direction.ASC, "vendorId");
        PageRequest request = PageRequest.of(0, 25, sort);

        return makeListFromIterable(
                vendorRepository.findByVendorIdOrName(search, search, request)
        );
    }

    /**
     * Delete a vendor given an ID
     * @param deleteId, ID of the vendor to delete
     */
    public void deleteById(String deleteId) {
        vendorRepository.deleteById(deleteId);
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
