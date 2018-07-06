package com.grizzly.categorymicro;

import com.grizzly.categorymicro.model.Category;
import com.grizzly.categorymicro.model.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.grizzly.grizlibrary.helpers.Helper.getPageRequest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    // Convert a Product object into a ProductDTO
    public CategoryDTO categoryToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO(category.getName(), category.getDescription(), category.getEnabled(),
                category.getProductCount());
        categoryDTO.setCategoryId(category.getCategoryId());

        return categoryDTO;
    }

    // This exists for mocking purposes - regular code should never call it
    public void setCategoryRepository(CategoryRepository newRepo) {
        this.categoryRepository = newRepo;
    }

    /**
     * Get all categories in the database, sorted by name and paginated to 25 categories at a time.
     * Currently only returns 1st page (can update with first param of PageRequest.of()
     * @return ArrayList of Category objs
     */
    public ArrayList<CategoryDTO> get(Integer pageIndex, String column_name) {
        PageRequest request = getPageRequest(pageIndex, column_name, "category", 25);
        Page<Category> categories = categoryRepository.findAll(request);

        ArrayList<CategoryDTO> result = new ArrayList<>();
        for (Category category : categories) {
            result.add(categoryToDTO(category));
        }

        return result;
    }

    /**
     * Update an existing category (based on a given ID) with a new name and description
     * @param id, ID of the category to update
     * @param name, new name to overwrite the category's old one
     * @param description, new description to overwrite the category's old one
     * @return the original category object; null if none was found
     */
    public CategoryDTO edit(Integer id, String name, String description) {
        // find the existing category
        Category category;
        try {
            category = categoryRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            return null;
        }

        // make the changes to the category
        category.setName(name);
        category.setDescription(description);

        // save the updated category
        categoryRepository.save(category);
        return categoryToDTO(category);
    }

    @Transactional
    public CategoryDTO addCategory(String name, String description) {
        Category created = categoryRepository.save(new Category(name, description));
        return categoryToDTO(created);
    }

    /**
     * Update an existing category (based on a given ID)
     * @param id, ID of the category to update
     * @param newBool, new status enabled/disabled of category
     */
    public CategoryDTO setEnabled(Integer id, Boolean newBool) {
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
        return categoryToDTO(category);
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
    public CategoryDTO getSingle(Integer id) {
        return categoryToDTO(categoryRepository.findById(id).get());
    }

    /**
     * Get a filtered list of categories, based on a given search string to match to name .
     * @param search, the string to match to name to filter the categories by
     * @return ArrayList of Category objs whose names match
     */
    public ArrayList<CategoryDTO> getFiltered(String search) {
        try {
            Integer categoryId = Integer.parseInt(search);

            // To meet the specifications of spitting out an ArrayList...
            ArrayList<CategoryDTO> category = new ArrayList<>();
            category.add(getSingle(categoryId));

            return category;
        } catch(NumberFormatException e) {
            Sort sort = new Sort(Sort.Direction.ASC, "categoryId");
            PageRequest request = PageRequest.of(0, 25, sort);

            List<Category> categories = categoryRepository.findByCategoryName(search, request);

            ArrayList<CategoryDTO> categoryResult = new ArrayList<>();
            for (Category category : categories) {
                categoryResult.add(categoryToDTO(category));
            }

            return categoryResult;
        }
    }
    /**
     * Get a list of vendors based on vendor IDs
     * @param ids, The list of Vendor ids that are to be fetched
     * @return ArrayList of Vendor objs whose IDs match ids
     */
    public ArrayList<CategoryDTO> getBatchbyId(List<String> ids) {
        List<Integer> categoryIds = new ArrayList<>();

        for (String id: ids){
            categoryIds.add(Integer.parseInt(id));
        }

        List<Category> vendors = categoryRepository.findByCategoryIdIn(categoryIds);

        ArrayList<CategoryDTO> categoryResults = new ArrayList<>();

        for (Category vendor : vendors) {
            categoryResults.add(categoryToDTO(vendor));
        }

        return categoryResults;
    }

    /**
     * Update a productCount when a product is added
     * @param catID, ID of the Category to increment count
     * @return HTTP status response only
     */
    public void updateProductCount(Integer catID, Integer newCount) {
        categoryRepository.updateCategoryCount(catID, newCount);
    }
}
