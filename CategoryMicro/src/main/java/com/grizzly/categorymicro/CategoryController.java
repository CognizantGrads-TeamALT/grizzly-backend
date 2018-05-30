package com.grizzly.categorymicro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Random;


@RestController

@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Return a list of all categories in the system
     * @return categories in a list
     */
    @GetMapping("/all/{column_name}")
    public ResponseEntity<ArrayList<Category>> getAll(@PathVariable(value="column_name") String column_name) {
        ArrayList<Category> categories = categoryService.getAll(column_name);

        // if no categories found
        if (categories == null || categories.isEmpty()) {
            return new ResponseEntity<>(categories, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello()
    {
        return new ResponseEntity<String>("Hello!", HttpStatus.OK);
    }

    /**
     * Update a given category (by ID) with the given new values
     * @param id, ID of the category to update
     * @param name, new name of the category to overwrite old one
     * @param description, new description of the category to overwrite old one
     * @return HTTP status response only
     */
    @RequestMapping(value="/update/{id}", method=RequestMethod.POST)
    public ResponseEntity update(@PathVariable(value="id") String id, @RequestParam String name, @RequestParam String description) {
        boolean status = categoryService.update(id, name, description);

        // status is 0 if the ID did not map to an existing category
        if (!status) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

}
