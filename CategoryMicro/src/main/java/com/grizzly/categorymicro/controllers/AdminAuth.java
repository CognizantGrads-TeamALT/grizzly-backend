package com.grizzly.categorymicro.controllers;

import com.grizzly.categorymicro.CategoryDTO;
import com.grizzly.categorymicro.CategoryService;
import com.grizzly.grizlibrary.errorhandling.ApiError;
import static com.grizzly.grizlibrary.helpers.Helper.buildResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class AdminAuth {
    @Autowired
    private CategoryService categoryService;

    private Boolean hasAccess(String userData) {
        try {
            JSONObject jsonObject = new JSONObject(userData);
            return jsonObject.get("role").equals("admin");
        } catch (Exception e) {
            System.out.println("oh snap.");
        }

        return false;
    }

    /**
     * adding a new category
     * @param categoryDTO
     * @return
     */
    @PutMapping("/add")
    public ResponseEntity addCategory(@RequestBody CategoryDTO categoryDTO, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

        CategoryDTO created = categoryService.addCategory(categoryDTO.getName(), categoryDTO.getDescription());

        if (created == null) {
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST, "Category was not saved",
                    "name: " + categoryDTO.getName() + " desc: " + categoryDTO.getDescription()));
        }

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Update a given category (by ID) with the given new values
     * @param id, ID of the category to update
     * @param request, the new category to overwrite old one
     * @return HTTP status response only
     */
    @PostMapping("/edit/{id}")
    public ResponseEntity edit(@PathVariable(value="id") Integer id, @RequestBody CategoryDTO request, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

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
     * Delete a category based on a given ID
     * @param id, ID of the vendor to delete
     * @return HTTP status response only
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCategory(@PathVariable(value="id") Integer id, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

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
     * Update a given category (by ID), enabling/disabling the item
     * @param id, ID of the category to update
     * @param request, a category DTO with the appropiate enabled value
     * @return HTTP status response only
     */
    @PostMapping("/setBlock/{id}")
    public ResponseEntity setBlock(@PathVariable(value="id") Integer id, @RequestBody CategoryDTO request, @RequestHeader(value="User-Data") String userData) {
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

        CategoryDTO category = categoryService.setEnabled(id, request.getEnabled());

        // null if the ID did not map to an existing category
        if (category == null) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "The category to block was not found.", "id: " + id + "; new block status: " + request.getEnabled()));
        }

        return new ResponseEntity<>(category, HttpStatus.OK);
    }
}
