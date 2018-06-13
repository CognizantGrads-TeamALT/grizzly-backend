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
    @GetMapping("/get/{pageIndex}/{column_name}")
    public ResponseEntity<ArrayList<Product>> get(@PathVariable(value="pageIndex") Integer pageIndex, @PathVariable(value="column_name") String column_name) {
        ArrayList<Product> products = productService.get(pageIndex, column_name);

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
    @GetMapping("/get/{id}")
    public ResponseEntity<ArrayList<Product>> getSingle(@PathVariable(value="id") Integer id) {
        ArrayList<Product> products = productService.getSingle(id);

        // no products found
        if (products == null || products.isEmpty()) {
            return new ResponseEntity<>(products, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Return a single product with imgs based on id
     * @param id, product ID
     * @return the product with imgs
     */
    @GetMapping("/getDetails/{id}")
    public ResponseEntity<ArrayList<ProductDTO>> getSingleWithImgs(@PathVariable(value="id") Integer id) {
        ArrayList<ProductDTO> products = productService.getSingleWithImgs(id);

        // no products found
        if (products == null || products.isEmpty()) {
            return new ResponseEntity<>(products, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Update a given product (by ID), enabling/disabling the item
     * @param id, ID of the product to update
     * @param ProductDTO, the new boolean
     * @return HTTP status response only
     */
    @PostMapping("/setBlock/{id}")
    public ResponseEntity<Product> setBlock(@PathVariable(value="id") Integer id, @RequestBody ProductDTO request) {
        Product product = productService.setEnabled(id, request.getEnabled());

        // null if the ID did not map to an existing category
        if (product == null) {
            return new ResponseEntity<>(product, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    /**
     * Disable all products by vendor ID
     * @param id, ID of the product to update
     * @return HTTP status response only
     */
    @PostMapping("/setBlockByVendor/{id}")
    public ResponseEntity setBlockByVendor(@PathVariable(value="id") Integer id) {
        try {
            productService.disableByVendorId(id);
        } catch (EmptyResultDataAccessException e) {
            // this ID didn't match any products
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Return a list of products in the system filtered by a given search string on ID or name
     * @param search, string to filter returned list on by ID or name
     * @return the filtered products in a list
     */
    @GetMapping("/search/{search}")
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
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProduct(@PathVariable(value="id") Integer id) {
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
    @PutMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody ProductDTO newProduct) {
        Product created = productService.add(newProduct);

        if (created == null) {
            return new ResponseEntity<>(created, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Get all products that are in a given category, paginated and sorted
     * @param catId, ID of the category to filter by
     * @param pageIndex, index of the page of results to fetch
     * @param column_name, name of the product field we are sorting by (defaults to product ID)
     * @return list of products in the given category
     */
    @GetMapping("/bycategory/{catId}/{pageIndex}/{column_name}")
    public ResponseEntity<ArrayList<Product>> getByCategory(@PathVariable(value="catId") Integer catId,
                                                          @PathVariable(value="pageIndex") Integer pageIndex,
                                                          @PathVariable(value="column_name") String column_name) {
        ArrayList<Product> prods = productService.getByCategory(catId, pageIndex, column_name);

        if (prods == null || prods.isEmpty()) {
            return new ResponseEntity<>(prods, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(prods, HttpStatus.OK);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<String>("Hello!", HttpStatus.OK);
    }
}
