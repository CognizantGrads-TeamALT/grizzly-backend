package com.grizzly.productmicro.controllers;

import java.security.NoSuchAlgorithmException;
import com.grizzly.grizlibrary.errorhandling.ApiError;
import static com.grizzly.grizlibrary.helpers.Helper.buildResponse;

import com.grizzly.productmicro.model.ProductDTO;
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

    /**
     * Returns "accessLevel"
     * @param userData
     * @return Integer
     *
     * 0 = un-authed
     * -1 = admin
     * else the Integer is vendorId
     */
    private Integer hasAccess(String userData) {
        try {
            JSONObject jsonObject = new JSONObject(userData);

            if (jsonObject.get("role").equals("admin"))
                return -1;
            else if (jsonObject.get("role").equals("vendor"))
                return (Integer) jsonObject.get("vendorId");
        } catch (Exception e) {
            return 0;
        }

        return 0;
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
        Integer accessLevel = hasAccess(userData);
        if (accessLevel == 0)
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));
        else if (accessLevel != -1) {
            newProduct.setVendorId(accessLevel);
            newProduct.setEnabled(false); // disabled by default for vendor.
        }

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
}
