package com.grizzly.productmicro.controllers;

import java.security.NoSuchAlgorithmException;
import com.grizzly.grizlibrary.errorhandling.ApiError;
import static com.grizzly.grizlibrary.helpers.Helper.buildResponse;

import com.grizzly.productmicro.ProductDTO;
import com.grizzly.productmicro.ProductService;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class AdminVendorAuth {
    @Autowired
    private ProductService productService;

    private Boolean hasAccess(String userData) {
        try {
            JSONObject jsonObject = new JSONObject(userData);
            return jsonObject.get("role").equals("admin") || jsonObject.get("role").equals("vendor");
        } catch (Exception e) {
            System.out.println("oh snap.");
        }

        return false;
    }

    /**
     * Add a new products based on a given DTO resource
     * @param newProduct, the new product to store in the database
     * @return the newly created product
     *
     * AUTHENTICATION (admin + vendor) @RequestHeader(value="User-Data", required=false) String userData
     */
    @PutMapping("/add")
    public ResponseEntity addProduct(@RequestBody ProductDTO newProduct, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

        ProductDTO created;
        try {
            created = productService.add(newProduct);
        }
        // saving image to file failed
        catch (NullPointerException e) {
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST,
                    "A null pointer exception occurred while writing image to file.",
                    e.getLocalizedMessage()));
        }
        catch (NoSuchAlgorithmException e) {
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST,
                    "The product's images failed to save.",
                    e.getLocalizedMessage())); // TODO: make this process transactional so that this doesn't give us image-less products
        }

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * View a detailed product,edit and saved in the database
     * @param productId, ID of the product we are editing
     * @param request
     * @return
     *
     * AUTHENTICATION (admin + vendor)
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
}
