package com.grizzly.categorymicro.controllers;

import com.grizzly.categorymicro.CategoryService;
import com.grizzly.grizlibrary.errorhandling.ApiError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.grizzly.grizlibrary.helpers.Helper.buildResponse;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class AdminVendorAuth {
    @Autowired
    private CategoryService categoryService;

    /**
     * Returns "accessLevel"
     * @param userData
     * @return Boolean
     *
     * true = admin or vendor
     * false = not admin or vendor
     */
    private Boolean hasAccess(String userData) {
        try {
            JSONObject jsonObject = new JSONObject(userData);
            return jsonObject.get("role").equals("admin") || jsonObject.get("role").equals("vendor");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Update a productCount when a product is added
     * @param catID, ID of the Category to increment count
     * @return HTTP status response only
     */
    @PostMapping(value="/updateCount/{catID}")
    public ResponseEntity incrementProductCount(@PathVariable(value="catID") String catID, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

        try {
            categoryService.incrementProductCount(Integer.parseInt(catID));
        } catch(NumberFormatException e){
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST, "increment product count failed",
                    "catID: " + catID));
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
