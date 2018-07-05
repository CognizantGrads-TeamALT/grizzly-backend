package com.grizzly.productmicro.controllers;

import java.util.ArrayList;
import java.util.Arrays;

import com.grizzly.grizlibrary.errorhandling.ApiError;
import static com.grizzly.grizlibrary.helpers.Helper.buildResponse;

import com.grizzly.productmicro.ProductDTO;
import com.grizzly.productmicro.ProductInventoryDTO;
import com.grizzly.productmicro.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class NoAuth {
    @Autowired
    private ProductService productService;

    /**
     * Get a list of products based on products IDs
     * @param ids, The comma-separated string-list of Product ids that are to be fetched
     * @return the matching vendors in a list
     *
     * NO AUTHENTICATION
     */
    @GetMapping("/batchFetch/{ids}")
    public ResponseEntity getBatch(@PathVariable(value="ids") String ids) {
        String[] request = ids.split(",");
        ArrayList<ProductDTO> products;
        try {
            products = productService.getBatchbyId(Arrays.asList(request));
        }
        // ids were entered into SQL null
        catch (IllegalArgumentException e) {
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST,
                    "A null set of IDs was received.",
                    "ids: " + ids));
        }

        // no vendors found
        if (products == null || products.isEmpty()) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND,
                    "No products were found.",
                    "ids: " + ids));
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Return a single product based on id
     * @param id, product ID
     * @return the product
     *
     * NO AUTHENTICATION
     */
    @GetMapping("/get/{id}")
    public ResponseEntity getSingle(@PathVariable(value="id") Integer id) {
        ArrayList<ProductDTO> products = productService.getSingle(id);

        // no product found
        if (products == null || products.isEmpty()) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "No product was found.", "id: " + id));
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Return a list of all products in the system
     * @return products in a list
     *
     * NO AUTHENTICATION
     */
    @GetMapping("/get/{pageIndex}/{column_name}")
    public ResponseEntity get(@PathVariable(value="pageIndex") Integer pageIndex, @PathVariable(value="column_name") String column_name) {
        ArrayList<ProductDTO> products = productService.get(pageIndex, column_name);

        // no products found
        if (products == null || products.isEmpty()) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "No products were found.", "pageIndex: " + pageIndex + "; column_name: " + column_name));
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Return a single product with imgs based on id
     * @param id, product ID
     * @return the product with imgs
     *
     * NO AUTHENTICATION TODO DELETE THIS METHOD
     */
    @GetMapping("/getDetails/{id}")
    public ResponseEntity getSingleWithImgs(@PathVariable(value="id") Integer id) {
        ProductDTO product = productService.getSingleById(id);

        // no product found
        if (product == null) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "No product was found.", "id: " + id));
        }

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    /**
     * getting product inventory
     * @param pageIndex
     * @param vendorId
     * @return
     *
     * NO AUTHENTICATION
     */
    @GetMapping("/getInventory/{pageIndex}/{vendorId}")
    public ResponseEntity getInventory(@PathVariable Integer pageIndex, @PathVariable Integer vendorId){
        ArrayList<ProductInventoryDTO> products = productService.getInventory(pageIndex,vendorId);

        if (products == null || products.isEmpty()){
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "No inventory was found.", "vendor id: " + vendorId));
        }


        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Return a list of products in the system filtered by a given search string on ID or name
     * @param search, string to filter returned list on by ID or name
     * @param pageIndex, index of the page of results to return (starts at 0)
     * @return the filtered products in a list
     *
     * NO AUTHENTICATION
     */
    @GetMapping("/search/{search}/{pageIndex}")
    public ResponseEntity getFiltered(@PathVariable(value="search") String search, @PathVariable(value="pageIndex") Integer pageIndex) {
        ArrayList<ProductDTO> products = productService.getFiltered(search, pageIndex);

        // no products found
        if (products == null || products.isEmpty()) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "No products were found.", "search: " + search));
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Get all products that are in a given category, paginated and sorted
     * @param catId, ID of the category to filter by
     * @param pageIndex, index of the page of results to fetch
     * @param column_name, name of the product field we are sorting by (defaults to product ID)
     * @return list of products in the given category
     *
     * NO AUTHENTICATION
     */
    @GetMapping("/byCategory/{catId}/{pageIndex}/{column_name}")
    public ResponseEntity getByCategory(@PathVariable(value="catId") Integer catId,
                                        @PathVariable(value="pageIndex") Integer pageIndex,
                                        @PathVariable(value="column_name") String column_name) {
        ArrayList<ProductDTO> products = productService.getByCategory(catId, pageIndex, column_name);

        if (products == null || products.isEmpty()) {
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST,
                    "No products were found.",
                    "catId: " + catId + "; pageIndex: " + pageIndex + "; column_name: " + column_name));
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
