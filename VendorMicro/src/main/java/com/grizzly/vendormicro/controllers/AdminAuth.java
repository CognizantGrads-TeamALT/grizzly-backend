package com.grizzly.vendormicro.controllers;

import com.grizzly.grizlibrary.errorhandling.ApiError;
import com.grizzly.vendormicro.Vendor;
import com.grizzly.vendormicro.VendorDTO;
import com.grizzly.vendormicro.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.grizzly.grizlibrary.helpers.Helper.buildResponse;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class AdminAuth {
    @Autowired
    private VendorService vendorService;

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
     * Add a new vendor based on a given DTO resource
     * @param newVendor, the new vendor to store in the database
     * @return the newly created vendor
     *
     * AUTHENTICATION (admin only)
     */
    @PutMapping("/add")
    public ResponseEntity addVendor(@RequestBody VendorDTO newVendor, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

        VendorDTO created;
        try {
            created = vendorService.add(newVendor);
        } catch (Exception e) {
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST,
                    "Failed to create a new vendor.",
                    e.getLocalizedMessage()));
        }

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Delete a vendor based on a given ID
     * @param id, ID of the vendor to delete
     * @return HTTP status response only
     *
     * AUTHENTICATION (admin only)
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteVendor(@PathVariable(value="id") Integer id, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

        try {
            vendorService.deleteById(id);
        }
        // this ID didn't match any vendors
        catch (EmptyResultDataAccessException e) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND,
                    "No vendor was found to delete.",
                    "id: " + id));
        }

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    /**
     * Update a given vendor (by ID), enabling/disabling the item
     * @param id, ID of the vendor to update
     * @param request, the new boolean
     * @return HTTP status response only
     *
     * AUTHENTICATION (admin only)
     */
    @PostMapping("/setBlock/{id}")
    public ResponseEntity setBlock(@PathVariable(value="id") Integer id, @RequestBody VendorDTO request, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

        Vendor vendor;
        try {
            vendor = vendorService.setEnabled(id, request.getEnabled());
        }
        // id was entered into SQL null
        catch (IllegalArgumentException e) {
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST,
                    "A null vendor ID was received.",
                    "id: " + id));
        }

        // null if the ID did not map to an existing vendor
        if (vendor == null) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND,
                    "No vendor was found to update.",
                    "id: " + id));
        }

        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }
}
