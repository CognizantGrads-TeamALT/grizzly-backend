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
    @GetMapping("/all")
    public ResponseEntity<ArrayList<Vendor>> getAll() {
        ArrayList<Vendor> vendors = vendorService.getAll();

        // no vendors found
//        if (vendors == null || vendors.isEmpty()) {
//            return new ResponseEntity<>(vendors, HttpStatus.NOT_FOUND);
//        }
        vendors.add(new Vendor("1", "Bob", "0456789219", "www.bob.com", "bob@website.com", "This is Bob"));
        vendors.add(new Vendor("2", "Jane", "0454329219", "www.jane.com", "jane@website.com", "This is Jane"));
        vendors.add(new Vendor("3", "Marty", "0447789249", "www.marty.com", "marty@website.com", "This is Marty"));

        return new ResponseEntity<>(vendors, HttpStatus.OK);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<String>("Hello!", HttpStatus.OK);
    }

}

