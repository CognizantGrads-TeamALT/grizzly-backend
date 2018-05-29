package com.grizzly.vendormicro;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
//        if (vendors == null || vendors.isEmpty()) {
//            return new ResponseEntity<>(vendors, HttpStatus.NOT_FOUND);
//        }
        vendors.add(new Vendor("1", "Bob", "0456789219", "www.bob.com", "bob@website.com", "This is Bob"));
        vendors.add(new Vendor("2", "Jane", "0454329219", "www.jane.com", "jane@website.com", "This is Jane"));
        vendors.add(new Vendor("3", "Marty", "0447789249", "www.marty.com", "marty@website.com", "This is Marty"));

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

        // no stats found
        if (vendors == null || vendors.isEmpty()) {
            return new ResponseEntity<>(vendors, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(vendors, HttpStatus.OK);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<String>("Hello!", HttpStatus.OK);
    }

}

