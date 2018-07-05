package com.grizzly.productmicro.controllers;

import com.grizzly.grizlibrary.errorhandling.ApiError;
import static com.grizzly.grizlibrary.helpers.Helper.buildResponse;

import com.grizzly.productmicro.ProductDTO;
import com.grizzly.productmicro.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class AdminAuth {
    @Autowired
    private ProductService productService;

    private Boolean hasAccess(String userData) {
        try {
            JSONObject jsonObject = new JSONObject(userData);
            return jsonObject.get("role").equals("admin");
        } catch (Exception e) {
            System.out.println("oh snap.");
        }

        return false;
    }

    /**
     * Delete a product based on a given ID
     * @param id, ID of the product to delete
     * @return HTTP status response only
     *
     * AUTHENTICATION (admin only)
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProduct(@PathVariable(value="id") Integer id, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

        try {
            productService.deleteById(id);
        }
        // this ID didn't match any products
        catch (EmptyResultDataAccessException e) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND,
                    "No product was found.",
                    "id: " + id + "; exception msg: " + e.getLocalizedMessage()));
        }

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    /**
     * Update a given product (by ID), enabling/disabling the item
     * @param id, ID of the product to update
     * @param request, the new boolean
     * @return HTTP status response only
     *
     * AUTHENTICATION (admin + vendor)
     */
    @PostMapping("/setBlock/{id}")
    public ResponseEntity setBlock(@PathVariable(value="id") Integer id, @RequestBody ProductDTO request, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

        ProductDTO product = productService.setEnabled(id, request.getEnabled());

        // null if the ID did not map to an existing category
        if (product == null) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "The product to block was not found.", "id: " + id + "; new block status: " + request.getEnabled()));
        }

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    /**
     * Disable all products by vendor ID
     * Also resets vendorId to 0.
     * @param id, ID of the product to update
     * @return HTTP status response only
     *
     * AUTHENTICATION (admin only)
     */
    @PostMapping("/setBlockByVendor/{id}")
    public ResponseEntity setBlockByVendor(@PathVariable(value="id") Integer id, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

        try {
            productService.disableByVendorId(id);
        }
        // this ID didn't match any vendors
        catch (EmptyResultDataAccessException e) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "The vendor to block products of was not found.", "id: " + id + "; exception msg: " + e.getLocalizedMessage()));
        }

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    /**
     * Disable all products by vendor ID
     * Also resets vendorId to 0.
     * @param id, ID of the product to update
     * @return HTTP status response only
     *
     * AUTHENTICATION (admin only)
     */
    @PostMapping("/setBlockByCategory/{id}")
    public ResponseEntity setBlockByCategory(@PathVariable(value="id") Integer id, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

        try {
            productService.disableByCategoryId(id);
        }
        // this ID didn't match any categories
        catch (EmptyResultDataAccessException e) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "The category to block products of was not found.", "id: " + id + "; exception msg: " + e.getLocalizedMessage()));
        }

        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
