package com.grizzly.vendormicro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     * @param pageIndex, index of the page of results to get (of size 25)
     * @param column_name, Vendor column to sort the results by. If doesn't match any column name, defaults to vendor ID
     * @return vendors in a list
     */
    @GetMapping("/get/{pageIndex}/{column_name}")
    public ResponseEntity get(@PathVariable(value="pageIndex") Integer pageIndex, @PathVariable(value="column_name") String column_name) {
        ArrayList<Vendor> vendors = vendorService.get(pageIndex, column_name);

        // no vendors found in this page
        if (vendors == null || vendors.isEmpty()) {
            return new ResponseEntity("No vendors were found.\npageIndex: " + pageIndex + "\ncolumn_name: " + column_name, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(vendors, HttpStatus.OK);
    }

    /**
     * Return a single vendor based on id
     * @param id, vendor ID
     * @return the vendor
     */
    @GetMapping("/get/{id}")
    public ResponseEntity getSingle(@PathVariable(value="id") Integer id) {
        ArrayList<Vendor> vendors = new ArrayList<Vendor>();
        try {
            vendors = vendorService.getSingle(id);
        }
        // ID was entered into SQL null
        catch(IllegalArgumentException e) {
            return new ResponseEntity("ID was passed to the database null. Received id via HTTP: " + id, HttpStatus.BAD_REQUEST);
        }

        // the vendor wasn't found
        if (vendors == null || vendors.isEmpty()) {
            return new ResponseEntity("No vendor was found. id: " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(vendors, HttpStatus.OK);
    }

    /**
     * Return a list of vendors in the system filtered by a given search string on ID or name
     * @param search, string to filter returned list on by ID or name
     * @return the filtered vendors in a list
     */
    @GetMapping("/search/{search}")
    public ResponseEntity<ArrayList<Vendor>> getFiltered(@PathVariable(value="search") String search) {
        ArrayList<Vendor> vendors = new ArrayList<Vendor>();
        try {
            vendors = vendorService.getFiltered(search);
        }
        // search was entered into SQL null
        catch(IllegalArgumentException e) {
            return new ResponseEntity("Search was passed to the database null. Received search via HTTP: " + search, HttpStatus.BAD_REQUEST);
        }

        // no vendors found
        if (vendors == null || vendors.isEmpty()) {
            return new ResponseEntity("No vendors were found. search: " + search, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(vendors, HttpStatus.OK);
    }
    /**
     * Get a list of vendors based on vendor IDs
     * @param ids, The list of Vendor ids that are to be fetched
     * @return the matching vendors in a list
     */
    @GetMapping("/batchFetch/{ids}")
    public ResponseEntity<ArrayList<Vendor>> getBatch(@PathVariable(value="ids") String ids) {
        String[] request = ids.split(",");
        ArrayList<Vendor> vendors = vendorService.getBatchbyId(Arrays.asList(request));

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
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteVendor(@PathVariable(value="id") Integer id) {
        try {
            vendorService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            // this ID didn't match any vendors
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Update a given vendor (by ID), enabling/disabling the item
     * @param id, ID of the vendor to update
     * @param request, the new boolean
     * @return HTTP status response only
     */
    @PostMapping("/setBlock/{id}")
    public ResponseEntity<Vendor> setBlock(@PathVariable(value="id") Integer id, @RequestBody VendorDTO request) {
        Vendor vendor = vendorService.setEnabled(id, request.getEnabled());

        // null if the ID did not map to an existing vendor
        if (vendor == null) {
            return new ResponseEntity<>(vendor, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(vendor, HttpStatus.OK);
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