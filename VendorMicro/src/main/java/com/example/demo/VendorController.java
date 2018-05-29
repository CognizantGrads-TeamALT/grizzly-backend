package com.example.demo;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vendor")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    /**
     * Return a list of all vendors in the system
     * @return vendors in a list
     */
    @RequestMapping(value = "/sort/{column_name}", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Vendor>> getAll(@PathVariable(value="column_name") String column_name)
    {
        ArrayList<Vendor> vendors = vendorService.getAll(column_name);

        // no stats found
        if (vendors == null || vendors.isEmpty())
        {
            return new ResponseEntity<>(vendors, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(vendors, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/hello")
    public ResponseEntity<String> hello()
    {
        return new ResponseEntity<String>("Hello!", HttpStatus.OK);
    }

}

