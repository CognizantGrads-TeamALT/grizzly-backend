package com.grizzly.categorymicro;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Get all categories in the database, sorted by name and paginated to 25 categories at a time.
     * Currently only returns 1st page (can update with first param of PageRequest.of()
     * @return ArrayList of Category objs
     */

    public ArrayList<Category> getAll(String column_name)
    {

       PageRequest request = getPageRequest(column_name);
        return makeListFromIterable(categoryRepository.findAll(request));

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
    public PageRequest getPageRequest(String column_name)
    {
        Sort sort;


        final String[] fields = {"categoryId", "name", "description", "enabled"};
        String sortField;
        if (Arrays.asList(fields).contains(column_name)) {
            sortField = column_name;
        } else {
            sortField = "categoryId";

        }
        sort = new Sort(Sort.Direction.ASC, sortField);

        PageRequest request = PageRequest.of(0, 25, sort);
        return request;
    }

    /**
     * Function to add category
     * @return
     */
    @Transactional
    public ArrayList<Category> getAllCategories()
    {
        return (ArrayList<Category>) categoryRepository.findAll();

    }


    @Transactional
    public void addCategory(String name, String description)
    {
        categoryRepository.save(new Category(name, description));

    }
}
