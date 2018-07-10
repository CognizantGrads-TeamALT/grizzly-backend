package com.grizzly.categorymicro.controllers;

import com.grizzly.categorymicro.model.CategoryDTO;
import com.grizzly.categorymicro.CategoryService;
import com.grizzly.grizlibrary.errorhandling.ApiError;
import static com.grizzly.grizlibrary.helpers.Helper.buildResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class NoAuth {
    @Autowired
    private CategoryService categoryService;

    /**
     * Return a single category based on id
     * @param id, category ID
     * @return the category
     */
    @GetMapping("/get/{id}")
    public ResponseEntity getSingle(@PathVariable(value="id") Integer id) {
        CategoryDTO category = categoryService.getSingle(id);

        // no categories found
        if (category == null) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "No Category was found.", "id: " + id));
        }

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    /**
     * Return a list of all categories in the system
     * @return categories in a list
     */
    @GetMapping("/get/{pageIndex}/{column_name}")
    public ResponseEntity get(@PathVariable(value="pageIndex") Integer pageIndex, @PathVariable(value="column_name") String column_name) {
        ArrayList<CategoryDTO> categories = categoryService.get(pageIndex, column_name);

        // if no categories found
        if (categories == null || categories.isEmpty()) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "No categories were found.", "pageIndex: " + pageIndex + "; column_name: " + column_name));
        }

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    /**
     * Get a list of vendors based on vendor IDs
     * @param ids, The list of Vendor ids that are to be fetched
     * @return the matching vendors in a list
     */
    @GetMapping("/batchFetch/{ids}")
    public ResponseEntity getBatch(@PathVariable(value="ids") String ids) {
        String[] request = ids.split(",");
        ArrayList<CategoryDTO> categories = categoryService.getBatchbyId(Arrays.asList(request));

        // no categories found
        if (categories == null || categories.isEmpty()) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "get Category names failed", "ids: " + ids));
        }

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    /**
     * Return a list of categories in the system filtered by a given search string on name
     * @param search, string to filter returned list on by name
     * @return the filtered categories in a list
     */
    @GetMapping("/search/{search}")
    public ResponseEntity getFiltered(@PathVariable(value="search") String search) {
        ArrayList<CategoryDTO> categories = categoryService.getFiltered(search);

        // no categories found
        if (categories == null || categories.isEmpty()) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "No categories were found.", "search: " + search));
        }

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    /**
     * Update a productCount when a product is added
     * @param catID, ID of the Category to increment count
     * @return HTTP status response only
     *
     * FOR USE BY PRODUCT MICRO FEIGN CLIENT
     */
    @PostMapping(value="/updateCount")
    public ResponseEntity incrementProductCount(@RequestParam("catID") Integer catID, @RequestParam("newCount") Integer newCount) {
        try {
            categoryService.updateProductCount(catID, newCount);
        } catch(NumberFormatException e){
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST, "Unable to update category count",
                    "catID: " + catID + " newCount: " + newCount));
        }

        return new ResponseEntity<>(newCount, HttpStatus.OK);
    }
}
