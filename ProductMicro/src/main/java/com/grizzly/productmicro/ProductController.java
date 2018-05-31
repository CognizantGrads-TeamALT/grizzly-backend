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
     * Return a list of all vendors in the system
     * @return vendors in a list
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
     * Return a list of vendors in the system filtered by a given search string on ID or name
     * @param search, string to filter returned list on by ID or name
     * @return the filtered vendors in a list
     */
    @RequestMapping("/search/{search}")
    public ResponseEntity<ArrayList<Product>> getFiltered(@PathVariable(value="search") String search) {
        ArrayList<Product> products = productService.getFiltered(search);

        // no vendors found
        if (products == null || products.isEmpty()) {
            return new ResponseEntity<>(products, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Delete a vendor based on a given ID
     * @param id, ID of the vendor to delete
     * @return HTTP status response only
     */
    @RequestMapping(value="/delete/{id}", method=RequestMethod.DELETE)
    public ResponseEntity deleteProduct(@PathVariable(value="id") String id) {
        try {
            productService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            // this ID didn't match any vendors
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Add a new vendor based on a given DTO resource
     * @param newVendor, the new vendor to store in the database
     * @return the newly created vendor
     */
    @RequestMapping(value="/add", method=RequestMethod.PUT)
    public ResponseEntity<Product> addVendor(@RequestBody ProductDTO newVendor) {
        Product created = productService.add(newVendor.toEntity());

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