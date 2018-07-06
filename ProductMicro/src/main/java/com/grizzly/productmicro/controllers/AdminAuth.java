package com.grizzly.productmicro.controllers;

import com.grizzly.grizlibrary.errorhandling.ApiError;
import static com.grizzly.grizlibrary.helpers.Helper.buildResponse;

import com.grizzly.productmicro.model.ProductDTO;
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

    /**
     * Returns "accessLevel"
     * @param userData
     * @return Integer
     *
     * true = admin
     * false = not admin
     */
    private Boolean hasAccess(String userData) {
        try {
            JSONObject jsonObject = new JSONObject(userData);
            return jsonObject.get("role").equals("admin");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * View a detailed product,edit and saved in the database
     * @param productId, ID of the product we are editing
     * @param request
     * @return
     *
     * AUTHENTICATION (admin only)
     */
    @PostMapping("/edit/{id}")
    public ResponseEntity edit(@PathVariable(value="id") Integer productId, @RequestBody ProductDTO request, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

        ProductDTO product;
        try {
            product = productService.edit(productId, request);
        }
        catch (Exception e) {
            // exception if the ID did not map to an existing product
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST, "Edit product Failed.",
                    "name: " + request.getName()
                            + "price: " + request.getPrice() +
                            "desc: " + request.getDesc() +
                            "categoryId: " + request.getCategoryId() +
                            "; exception msg: " + e.getCause()));
        }

        return new ResponseEntity<>(product, HttpStatus.OK);
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
    @PostMapping("/setBlockByVendor/{id}/{block}")
    public ResponseEntity setBlockByVendor(@PathVariable(value="id") Integer id,@PathVariable(value="block") boolean block, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

        try {
            productService.disableByVendorId(id, block);
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
    @PostMapping("/setBlockByCategory/{id}/{block}")
    public ResponseEntity setBlockByCategory(@PathVariable(value="id") Integer id,@PathVariable(value="block") boolean block, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

        try {
            productService.disableByCategoryId(id, block);
        }
        // this ID didn't match any categories
        catch (EmptyResultDataAccessException e) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "The category to block products of was not found.", "id: " + id + "; exception msg: " + e.getLocalizedMessage()));
        }

        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
