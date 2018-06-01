package com.grizzly.vendormicro;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.NestedServletException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/vendor")
public class VendorController {
    @Autowired
    private VendorService vendorService;

    /**
     * Return a list of all vendors in the system
     * @return vendors in a list
     */
    @GetMapping("/all/{column_name}")
    public ResponseEntity<ArrayList<Vendor>> getAll(@PathVariable(value="column_name") String column_name) {
        ArrayList<Vendor> vendors = vendorService.getAll(column_name);

        // no vendors found
        if (vendors == null || vendors.isEmpty()) {
            return new ResponseEntity<>(vendors, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(vendors, HttpStatus.OK);
    }

    /**
     * Return a single vendor based on id
     * @param id, vendor ID
     * @return the vendor
     */
    @RequestMapping("/get/{id}")
    public ResponseEntity<ArrayList<Vendor>> getSingle(@PathVariable(value="id") Integer id) {
        ArrayList<Vendor> vendors = vendorService.getSingle(id);

        // no vendors found
        if (vendors == null || vendors.isEmpty()) {
            return new ResponseEntity<>(vendors, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(vendors, HttpStatus.OK);
    }

    /**
     * Return a list of vendors in the system filtered by a given search string on ID or name
     * @param search, string to filter returned list on by ID or name
     * @return the filtered vendors in a list
     */
    @RequestMapping("/search/{search}")
    public ResponseEntity<ArrayList<Vendor>> getFiltered(@PathVariable(value="search") String search) {
        ArrayList<Vendor> vendors = vendorService.getFiltered(search);

        // no vendors found
        if (vendors == null || vendors.isEmpty()) {
            return new ResponseEntity<>(vendors, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(vendors, HttpStatus.OK);
    }

    /**
     * Delete a vendor based on a given ID
     * @param id, ID of the vendor to delete
     * @return HTTP status response only
     */
    @RequestMapping(value="/delete/{id}", method=RequestMethod.DELETE)
    public ResponseEntity deleteVendor(@PathVariable(value="id") String id) {
        try {
            vendorService.deleteById(id);
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
    @PutMapping("/add")
    public ResponseEntity<Vendor> addVendor(@RequestBody VendorDTO newVendor) {
        Vendor created = vendorService.add(newVendor.toEntity());

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