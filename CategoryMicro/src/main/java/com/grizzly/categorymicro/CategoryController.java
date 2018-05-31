package com.grizzly.categorymicro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;



@RestController
@CrossOrigin(origins = "*")
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


    //@RequestMapping(value="/category", method= RequestMethod.GET)
    //public ArrayList<Category> getAllCategories()
    //{
    //    return categoryService.getAllCategories();
    //}

    @PutMapping("/add")
    public void addCategory(@RequestBody Request request)
    {
         categoryService.addCategory(request.getName(),request.getDescription());


    }


    /**
     * Return a list of categories in the system filtered by a given search string on name
     * @param search, string to filter returned list on by name
     * @return the filtered categories in a list
     */
    @RequestMapping("/search/{search}")
    public ResponseEntity<ArrayList<Category>> getFiltered(@PathVariable(value="search") String search)
    {
        ArrayList<Category> categories = categoryService.getFiltered(search);

        // no categories found
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
    @RequestMapping(value="/edit/{id}", method=RequestMethod.POST)
    public ResponseEntity edit(@PathVariable(value="id") String id, @RequestParam String name, @RequestParam String description) {
        Category newCat = categoryService.edit(id, name, description);

        // null if the ID did not map to an existing category
        if (newCat == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.OK);
    }


}
