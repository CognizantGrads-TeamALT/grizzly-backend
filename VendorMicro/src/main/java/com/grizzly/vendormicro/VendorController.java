package com.grizzly.vendormicro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.grizzly.grizlibrary.errorhandling.ApiError;
import com.grizzly.vendormicro.errorhandling.RestExceptionHandler;
import com.grizzly.vendormicro.image.ImageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.NestedServletException;

import static com.grizzly.grizlibrary.helpers.Helper.buildResponse;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
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
     * Return a single vendor based on id
     * @param id, vendor ID
     * @return the vendor
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
     * Return a single image from a vendor
     * @param fileName
     * @return the product with imgs
     */
    @GetMapping("/getImage/{fileName}")
    public ResponseEntity getSingleImage(@PathVariable(value="fileName") String fileName) {
        ImageDTO image = vendorService.getImageFromVendor(fileName);

        if (image == null) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "No image was found.", "filename: " + fileName));
        }

        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    /**
     * Return a list of vendors in the system filtered by a given search string on ID or name
     * @param search, string to filter returned list on by ID or name
     * @return the filtered vendors in a list
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
    /**
     * Get a list of vendors based on vendor IDs
     * @param ids, The comma-separated string-list of Vendor ids that are to be fetched
     * @return the matching vendors in a list
     */
    @GetMapping("/batchFetch/{ids}")
    public ResponseEntity getBatch(@PathVariable(value="ids") String ids) {
        String[] request = ids.split(",");
        ArrayList<VendorDTO> vendors;
        try {
            vendors = vendorService.getBatchbyId(Arrays.asList(request));
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
     * Delete a vendor based on a given ID
     * @param id, ID of the vendor to delete
     * @return HTTP status response only
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteVendor(@PathVariable(value="id") Integer id) {
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
     */
    @PostMapping("/setBlock/{id}")
    public ResponseEntity setBlock(@PathVariable(value="id") Integer id, @RequestBody VendorDTO request) {
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

    /**
     * Add a new vendor based on a given DTO resource
     * @param newVendor, the new vendor to store in the database
     * @return the newly created vendor
     */
    @PutMapping("/add")
    public ResponseEntity addVendor(@RequestBody VendorDTO newVendor) {
        VendorDTO created;
        try {
            created = vendorService.add(newVendor);
        } catch (NullPointerException e) {
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST,
                    "A null pointer exception occurred while writing image to file.",
                    e.getLocalizedMessage()));
        }

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/hello")
    public ResponseEntity hello() {
        return new ResponseEntity<>("Hello!", HttpStatus.OK);
    }
}