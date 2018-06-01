package com.grizzly.categorymicro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    @GetMapping("/get/{pageIndex}/{column_name}")
    public ResponseEntity<ArrayList<Category>> get(@PathVariable(value="pageIndex") String pageIndex, @PathVariable(value="column_name") String column_name) {
        ArrayList<Category> categories = categoryService.get(pageIndex, column_name);

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
    public void addCategory(@RequestBody CategoryDTO categoryDTO) {
         categoryService.addCategory(categoryDTO.getName(), categoryDTO.getDescription());
    }

    /**
     * Return a single category based on id
     * @param id, category ID
     * @return the category
     */
    @RequestMapping("/get/{id}")
    public ResponseEntity<ArrayList<Category>> getSingle(@PathVariable(value="id") Integer id) {
        ArrayList<Category> categories = categoryService.getSingle(id);

        // no categories found
        if (categories == null || categories.isEmpty()) {
            return new ResponseEntity<>(categories, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    /**
     * Return a list of categories in the system filtered by a given search string on name
     * @param search, string to filter returned list on by name
     * @return the filtered categories in a list
     */
    @RequestMapping("/search/{search}")
    public ResponseEntity<ArrayList<Category>> getFiltered(@PathVariable(value="search") String search) {
        ArrayList<Category> categories = categoryService.getFiltered(search);

        // no categories found
        if (categories == null || categories.isEmpty()) {
            return new ResponseEntity<>(categories, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<String>("Hello!", HttpStatus.OK);
    }

    /**
     * Update a given category (by ID) with the given new values
     * @param id, ID of the category to update
     * @param request, the new category to overwrite old one
     * @return HTTP status response only
     */
    @RequestMapping(value="/edit/{id}", method=RequestMethod.POST)
    public ResponseEntity edit(@PathVariable(value="id") String id, @RequestBody CategoryDTO request) {
        Category newCat = categoryService.edit(id, request.getName(), request.getDescription());

        // null if the ID did not map to an existing category
        if (newCat == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Delete a vendor based on a given ID
     * @param id, ID of the vendor to delete
     * @return HTTP status response only
     */
    @RequestMapping(value="/delete/{id}", method=RequestMethod.DELETE)
    public ResponseEntity deleteCategory(@PathVariable(value="id") String id) {
        try {
            categoryService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            // this ID didn't match any vendors
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
