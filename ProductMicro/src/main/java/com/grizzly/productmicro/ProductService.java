package com.grizzly.productmicro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;


    public ArrayList<Product> getAll(String column_name) {
       PageRequest request = getPageRequest(column_name);
       return makeListFromIterable(productRepository.findAll(request));
    }

    /**
    * Utility function to generate a pagerequest to tell the database how to page and sort a query
    * @param column_name, the fieldname in the database to sort the list
    * @return pageRequest to the method called
    */
    public PageRequest getPageRequest(String column_name) {
        final String[] fields = {"productId", "name", "vendorId", "categoryId", "desc", "price", "enabled"};
        String sortField;
        if (Arrays.asList(fields).contains(column_name)) {
            sortField = column_name;
        } else {
            sortField = "productId";
        }

        Sort sort = new Sort(Sort.Direction.ASC, sortField);

        PageRequest request = PageRequest.of(0, 25, sort);
        return request;
    }

    /**
     * Get a single item from product id.
     * @param search, the string to match to ID to filter the product by
     * @return ArrayList of Product objs whose names or IDs
     */
    public ArrayList<Product> getSingle(String search) {
        Sort sort = new Sort(Sort.Direction.ASC, "productId");
        PageRequest request = PageRequest.of(0, 25, sort);

        return makeListFromIterable(
                productRepository.findByProductId(search, request)
        );
    }

    /**
     * Get a filtered list of products, based on a given search string to match to name or ID.
     * @param search, the string to match to name or ID to filter the products by
     * @return ArrayList of Product objs whose names or IDs
     */
    public ArrayList<Product> getFiltered(String search) {
        Sort sort = new Sort(Sort.Direction.ASC, "productId");
        PageRequest request = PageRequest.of(0, 25, sort);

        return makeListFromIterable(
                productRepository.findByProductIdOrName(search, search, request)
        );
    }

    /**
     * Add a new product to the database
     * @param newProduct, the entity of the new product to save
     * @return the added product object
     */
    public Product add(Product newProduct) {
        Product created = productRepository.save(newProduct);
        return created;
    }

    /**
     * Delete a product given an ID
     * @param deleteId, ID of the product to delete
     */
    public void deleteById(String deleteId) {
        productRepository.deleteById(deleteId);
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