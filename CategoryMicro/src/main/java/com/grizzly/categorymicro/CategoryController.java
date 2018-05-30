package com.grizzly.categorymicro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;



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


   //@RequestMapping(value="/category", method= RequestMethod.GET)
    //public ArrayList<Category> getAllCategories()
//    {
//        return categoryService.getAllCategories();
//    }

    @RequestMapping(value="/add", method= RequestMethod.PUT)
    public void addCategory(@RequestParam String name, @RequestParam String description)
    {
//        categoryService.addCategory(name,description);
        categoryService.addCategory("abc","description of abc");
        categoryService.addCategory("XYZ","description of xyz");

    }


    @GetMapping("/hello")
    public ResponseEntity<String> hello()
    {
        return new ResponseEntity<String>("Hello!", HttpStatus.OK);
    }




}
