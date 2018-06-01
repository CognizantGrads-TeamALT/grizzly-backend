package com.grizzly.productmicro;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.util.NestedServletException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     * Return a list of all products in the system
     * @return products in a list
     */
    @GetMapping("/all/{column_name}")
    public ResponseEntity<ArrayList<Product>> getAll(@PathVariable(value="column_name") String column_name) {
        ArrayList<Product> products = productService.getAll(column_name);

        // no products found
        if (products == null || products.isEmpty()) {
            return new ResponseEntity<>(products, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Return a single product based on id
     * @param id, product ID
     * @return the product
     */
    @RequestMapping("/get/{id}")
    public ResponseEntity<ArrayList<Product>> getSingle(@PathVariable(value="id") Integer id) {
        ArrayList<Product> products = productService.getSingle(id);

        // no products found
        if (products == null || products.isEmpty()) {
            return new ResponseEntity<>(products, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Return a list of products in the system filtered by a given search string on ID or name
     * @param search, string to filter returned list on by ID or name
     * @return the filtered products in a list
     */
    @RequestMapping("/search/{search}")
    public ResponseEntity<ArrayList<Product>> getFiltered(@PathVariable(value="search") String search) {
        ArrayList<Product> products = productService.getFiltered(search);

        // no products found
        if (products == null || products.isEmpty()) {
            return new ResponseEntity<>(products, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Delete a product based on a given ID
     * @param id, ID of the product to delete
     * @return HTTP status response only
     */
    @RequestMapping(value="/delete/{id}", method=RequestMethod.DELETE)
    public ResponseEntity deleteProduct(@PathVariable(value="id") String id) {
        try {
            productService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            // this ID didn't match any products
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Add a new products based on a given DTO resource
     * @param newProduct, the new product to store in the database
     * @return the newly created product
     */
    @RequestMapping(value="/add", method=RequestMethod.PUT)
    public ResponseEntity<Product> addProduct(@RequestBody ProductDTO newProduct) {
        Product created = productService.add(newProduct.toEntity());

        if (created == null) {
            return new ResponseEntity<>(created, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<String>("Hello!", HttpStatus.OK);
    }
}