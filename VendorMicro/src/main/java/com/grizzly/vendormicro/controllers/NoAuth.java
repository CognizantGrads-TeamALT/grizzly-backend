package com.grizzly.vendormicro.controllers;

import java.util.ArrayList;
import java.util.Arrays;

import com.grizzly.grizlibrary.errorhandling.ApiError;
import com.grizzly.vendormicro.model.VendorDTO;
import com.grizzly.vendormicro.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.grizzly.grizlibrary.helpers.Helper.buildResponse;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class NoAuth {
    @Autowired
    private VendorService vendorService;

    /**
     * Return a single vendor based on id
     * @param id, vendor ID
     * @return the vendor
     *
     * NO AUTHENTICATION
     */
    @GetMapping("/get/{id}")
    public ResponseEntity getSingle(@PathVariable(value="id") Integer id) {
        ArrayList<VendorDTO> vendors;
        try {
            vendors = vendorService.getSingle(id);
        }
        // ID was entered into SQL null
        catch(IllegalArgumentException e) {
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST,
                    "A null vendor ID was received.",
                    "id: " + id));
        }

        // the vendor wasn't found
        if (vendors == null || vendors.isEmpty()) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND,
                    "No vendor was found.",
                    "id: " + id));
        }

        return new ResponseEntity<>(vendors, HttpStatus.OK);
    }

    /**
     * Return a list of all vendors in the system
     * @param pageIndex, index of the page of results to get (of size 25)
     * @param column_name, Vendor column to sort the results by. If doesn't match any column name, defaults to vendor ID
     * @return vendors in a list
     *
     * NO AUTHENTICATION
     */
    @GetMapping("/get/{pageIndex}/{column_name}")
    public ResponseEntity get(@PathVariable(value="pageIndex") Integer pageIndex, @PathVariable(value="column_name") String column_name, @RequestHeader(value="User-Data") String userData) {
        ArrayList<VendorDTO> vendors = vendorService.get(pageIndex, column_name);

        // no vendors found in this page
        if (vendors == null || vendors.isEmpty()) {
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST,
                    "No vendors were found.",
                    "pageIndex: " + pageIndex + "\ncolumn_name: " + column_name));
        }

        return new ResponseEntity<>(vendors, HttpStatus.OK);
    }

    /**
     * Get a list of vendors based on vendor IDs
     * @param ids, The comma-separated string-list of Vendor ids that are to be fetched
     * @return the matching vendors in a list
     *
     * NO AUTHENTICATION
     */
    @GetMapping("/batchFetch/{ids}")
    public ResponseEntity getBatch(@PathVariable(value="ids") String ids) {
        String[] request = ids.split(",");
        ArrayList<VendorDTO> vendors;
        try {
            vendors = vendorService.getBatchById(Arrays.asList(request));
        }
        // ids were entered into SQL null
        catch (IllegalArgumentException e) {
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST,
                    "A null set of IDs was received.",
                    "ids: " + ids));
        }

        // no vendors found
        if (vendors == null || vendors.isEmpty()) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND,
                    "No vendors were found.",
                    "ids: " + ids));
        }

        return new ResponseEntity<>(vendors, HttpStatus.OK);
    }

    /**
     * Return a list of vendors in the system filtered by a given search string on ID or name
     * @param search, string to filter returned list on by ID or name
     * @return the filtered vendors in a list
     *
     * NO AUTHENTICATION
     */
    @GetMapping("/search/{search}")
    public ResponseEntity getFiltered(@PathVariable(value="search") String search) {
        ArrayList<VendorDTO> vendors;
        try {
            vendors = vendorService.getFiltered(search);
        }
        // search was entered into SQL null
        catch(IllegalArgumentException e) {
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST,
                    "A null search string was received.",
                    "search: " + search));
        }

        // no vendors found
        if (vendors == null || vendors.isEmpty()) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND,
                    "No vendors were found.",
                    "search: " + search));
        }

        return new ResponseEntity<>(vendors, HttpStatus.OK);
    }
}
