package com.example.demo;

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

    /**
     * Get all vendors in the database
     * @return ArrayList of Vendor objs
     */


    public ArrayList<Vendor> getAll(String column_name)
    {

       PageRequest request = getPageRequest(column_name);
       return makeListFromIterable(vendorRepository.findAll(request));


  }

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
