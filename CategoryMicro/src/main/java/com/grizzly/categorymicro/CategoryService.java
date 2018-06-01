package com.grizzly.categorymicro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    // This exists for mocking purposes - regular code should never call it
    public void setCategoryRepository(CategoryRepository newRepo) {
        this.categoryRepository = newRepo;
    }

    /**
     * Get all categories in the database, sorted by name and paginated to 25 categories at a time.
     * Currently only returns 1st page (can update with first param of PageRequest.of()
     * @return ArrayList of Category objs
     */

    public ArrayList<Category> getAll(String column_name) {
       PageRequest request = getPageRequest(column_name);
        return makeListFromIterable(categoryRepository.findAll(request));
    }

    /**
     * Update an existing category (based on a given ID) with a new name and description
     * @param id, ID of the category to update
     * @param name, new name to overwrite the category's old one
     * @param description, new description to overwrite the category's old one
     * @return the original category object; null if none was found
     */
    public Category edit(String id, String name, String description) {
        // find the existing category
        Category cat;
        try {
            cat = categoryRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            return null;
        }

        // make the changes to the category
        cat.setName(name);
        cat.setDescription(description);

        // save the updated category
        categoryRepository.save(cat);
        return cat;
    }

    public static <T> ArrayList<T> makeListFromIterable(Iterable<T> iter) {
        ArrayList<T> list = new ArrayList<T>();

        for(T item: iter) {
            list.add(item);
        }

        return list;
    }

    /**
     * Utility function to generate a pagerequest to tell the database how to page and sort a query
     * @param column_name, the fieldname in the database to sort the list
     * @return pageRequest to the method called
     */
    public PageRequest getPageRequest(String column_name) {
        final String[] fields = {"categoryId", "name", "description", "enabled"};
        String sortField;
        if (Arrays.asList(fields).contains(column_name)) {
            sortField = column_name;
        } else {
            sortField = "categoryId";
        }

        Sort sort = new Sort(Sort.Direction.ASC, sortField);

        PageRequest request = PageRequest.of(0, 25, sort);
        return request;
    }

    @Transactional
    public void addCategory(String name, String description) {
        categoryRepository.save(new Category(name, description));
    }

    /**
     * Delete a vendor given an ID
     * @param deleteId, ID of the vendor to delete
     */
    public void deleteById(String deleteId) {
        categoryRepository.deleteById(deleteId);
    }

    /**
     * Get a single item from product id.
     * @param id, the string to match to ID to filter the product by
     * @return ArrayList of Category
     */
    public ArrayList<Category> getSingle(Integer id) {
        Sort sort = new Sort(Sort.Direction.ASC, "categoryId");
        PageRequest request = PageRequest.of(0, 25, sort);

        return makeListFromIterable(
                categoryRepository.findByCategoryId(id, request)
        );
    }

    /**
     * Get a filtered list of categories, based on a given search string to match to name .
     * @param search, the string to match to name to filter the categories by
     * @return ArrayList of Category objs whose names
     */
    public ArrayList<Category> getFiltered(String search) {
        try {
            Integer categoryId = Integer.parseInt(search);

            return getSingle(categoryId);
        } catch(NumberFormatException e) {
            Sort sort = new Sort(Sort.Direction.ASC, "categoryId");
            PageRequest request = PageRequest.of(0, 25, sort);

            return makeListFromIterable(
                    categoryRepository.findByCategoryName(search, request)
            );
        }
    }

    /**
     * Update a productCount when a product is added
     * @param catID, ID of the Category to increment count
     * @return HTTP status response only
     */
    public void incrementProductCount(int catID){


    }
}
