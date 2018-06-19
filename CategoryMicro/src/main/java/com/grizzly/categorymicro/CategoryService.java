package com.grizzly.categorymicro;

import com.grizzly.grizlibrary.helpers.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.grizzly.grizlibrary.helpers.Helper.makeListFromIterable;
import static com.grizzly.grizlibrary.helpers.Helper.getPageRequest;

import javax.validation.constraints.Null;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public ArrayList<Category> get(Integer pageIndex, String column_name) {
       PageRequest request = getPageRequest(pageIndex, column_name,"category");
        return makeListFromIterable(categoryRepository.findAll(request));
    }

    /**
     * Update an existing category (based on a given ID) with a new name and description
     * @param id, ID of the category to update
     * @param name, new name to overwrite the category's old one
     * @param description, new description to overwrite the category's old one
     * @return the original category object; null if none was found
     */
    public Category edit(Integer id, String name, String description) {
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

    @Transactional
    public Category addCategory(String name, String description) {
        Category created = categoryRepository.save(new Category(name, description));
        return created;
    }

    /**
     * Update an existing category (based on a given ID)
     * @param id, ID of the category to update
     * @param newBool, new status enabled/disabled of category
     */
    public Category setEnabled(Integer id, Boolean newBool) {
        // find the existing category
        Category category;
        try {
            category = categoryRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            return null;
        }

        // make changes
        category.setEnabled(newBool);

        // save the updated category
        categoryRepository.save(category);
        return category;
    }

    /**
     * Delete a vendor given an ID
     * @param deleteId, ID of the vendor to delete
     */
    public void deleteById(Integer deleteId) {
        categoryRepository.deleteById(deleteId);
    }

    /**
     * Get a single item from product id.
     * @param id, the string to match to ID to filter the product by
     * @return Category
     */
    public Category getSingle(Integer id) {
        return categoryRepository.findById(id).get();
    }

    /**
     * Get a filtered list of categories, based on a given search string to match to name .
     * @param search, the string to match to name to filter the categories by
     * @return ArrayList of Category objs whose names match
     */
    public ArrayList<Category> getFiltered(String search) {
        try {
            Integer categoryId = Integer.parseInt(search);

            // To meet the specifications of spitting out an ArrayList...
            ArrayList<Category> category = new ArrayList<>();
            category.add(getSingle(categoryId));

            return category;
        } catch(NumberFormatException e) {
            Sort sort = new Sort(Sort.Direction.ASC, "categoryId");
            PageRequest request = PageRequest.of(0, 25, sort);

            return makeListFromIterable(
                    categoryRepository.findByCategoryName(search, request)
            );
        }
    }
    /**
     * Get a list of vendors based on vendor IDs
     * @param ids, The list of Vendor ids that are to be fetched
     * @return ArrayList of Vendor objs whose IDs match ids
     */
    public ArrayList<Category> getBatchbyId(List<String> ids) {
        List<Integer> ints = new ArrayList<Integer>();

        for(String id: ids){
            ints.add(Integer.parseInt(id));
        }
        ArrayList<Category> batchList = makeListFromIterable(categoryRepository.findByCategoryIdIn(ints));
        if(ints.size() != batchList.size()){
            //invalid category passed in.
            for (int i: ints) {
                if(!hasCategory(batchList,i)){
                    Category newcat = new Category();
                    newcat.setCategoryId(i);
                    newcat.setName("not found");
                    newcat.setDescription("category not found");
                    batchList.add(newcat);
                }

            }
        }

        return batchList;
    }

    private boolean hasCategory(ArrayList<Category> categorys, int i){
        for(Category category:categorys){
            if(category.getCategoryId() == i)
                return true;
        }
        return false;
    }

    /**
     * Update a productCount when a product is added
     * @param catID, ID of the Category to increment count
     * @return HTTP status response only
     */
    public void incrementProductCount(int catID){
        categoryRepository.incrementProductCount(catID);

    }
}
