package com.grizzly.grizlibrary.helpers;

import com.grizzly.grizlibrary.errorhandling.ApiError;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;

public class Helper {

    // If a field can be used as a sort anchor, it needs to be in here.
    private static final String[] vendorFields = {"vendorId", "name", "contactNum", "website", "email", "bio"};
    private static final String[] productFields = {"productId", "name", "vendorId", "categoryId", "desc", "price", "rating", "enabled"};
    private static final String[] categoryFields = {"categoryId", "name", "description"};
    /**
     * Utility function to generate a pagerequest to tell the database how to page and sort a query
     * @param column_name, the fieldname in the database to sort the list
     * @param table, the name of the table which this PageRequest will operate on
     * @param pageSize, the number of items on each page
     * @return pageRequest to the method called, null if an unrecognised table is provided
     */
    public static PageRequest getPageRequest(Integer pageIndex, String column_name, String table, Integer pageSize) {
        String[] fields = {};
        switch (table) {
            case "vendor":
                fields = vendorFields;
                break;
            case "product":
                fields = productFields;
                break;
            case "category":
                fields = categoryFields;
                break;
            default:
                final String[] validTables = {"vendor", "product"};
                throw new IllegalArgumentException("The provided table name (" + table + ") is invalid. Valid ones are: " + validTables.toString() + ".");
        }


        String sortField;
        if (Arrays.asList(fields).contains(column_name)) {
            sortField = column_name;
        }
        else {
            sortField = fields[0];
        }

        Sort sort = new Sort(Sort.Direction.ASC, sortField);
        PageRequest request = PageRequest.of(pageIndex, pageSize, sort);
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

    /**
     * Helper method to quickly build response entities from ApiErrors
     * @param apiError, the error to build a response out of
     * @return a response entity containing the ApiError
     */
    public static ResponseEntity<Object> buildResponse(ApiError apiError) {
        return new ResponseEntity<Object>(apiError, apiError.getStatus());
    }
}
