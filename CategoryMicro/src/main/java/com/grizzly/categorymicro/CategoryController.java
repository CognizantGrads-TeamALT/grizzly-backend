package com.grizzly.categorymicro;

import com.grizzly.grizlibrary.errorhandling.ApiError;
import static com.grizzly.grizlibrary.helpers.Helper.buildResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

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

    @PutMapping("/add")
    public ResponseEntity addCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO created = categoryService.addCategory(categoryDTO.getName(), categoryDTO.getDescription());

        if (created == null) {
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST, "Category was not saved",
                    "name: " + categoryDTO.getName() + " desc: " + categoryDTO.getDescription()));
        }

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

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
     * Update a given category (by ID), enabling/disabling the item
     * @param id, ID of the category to update
     * @param request, a category DTO with the appropiate enabled value
     * @return HTTP status response only
     */
    @PostMapping("/setBlock/{id}")
    public ResponseEntity setBlock(@PathVariable(value="id") Integer id, @RequestBody CategoryDTO request) {
        CategoryDTO category = categoryService.setEnabled(id, request.getEnabled());

        // null if the ID did not map to an existing category
        if (category == null) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "The category to block was not found.", "id: " + id + "; new block status: " + request.getEnabled()));
        }

        return new ResponseEntity<>(category, HttpStatus.OK);
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
    @PostMapping("/edit/{id}")
    public ResponseEntity edit(@PathVariable(value="id") Integer id, @RequestBody CategoryDTO request) {
        CategoryDTO category = categoryService.edit(id, request.getName(), request.getDescription());

        // null if the ID did not map to an existing category
        if (category == null) {
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST, "Edit product Failed.",
                    "name: " + request.getName() +
                    "desc: " + request.getDescription() ));
        }

        return new ResponseEntity<>(category, HttpStatus.OK);
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
     * Delete a category based on a given ID
     * @param id, ID of the vendor to delete
     * @return HTTP status response only
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCategory(@PathVariable(value="id") Integer id) {
        try {
            categoryService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            // this ID didn't match any category
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND,
                    "No category was found to delete.",
                    "id: " + id));
        }

        return new ResponseEntity(HttpStatus.OK);
    }


    /**
     * Update a productCount when a product is added
     * @param catID, ID of the Category to increment count
     * @return HTTP status response only
     */
    @PostMapping(value="/updateCount/{catID}")
    public ResponseEntity incrementProductCount(@PathVariable(value="catID") String catID){
        try{
            categoryService.incrementProductCount(Integer.parseInt(catID));
        } catch(NumberFormatException e){
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST, "increment product count failed",
                    "catiD: " + catID));
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
