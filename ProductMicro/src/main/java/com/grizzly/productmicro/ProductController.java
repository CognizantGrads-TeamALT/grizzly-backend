package com.grizzly.productmicro;

import java.util.ArrayList;

import com.grizzly.grizlibrary.errorhandling.ApiError;
import static com.grizzly.grizlibrary.helpers.Helper.buildResponse;

import com.grizzly.productmicro.image.ImageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Null;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     * Return a list of all products in the system
     * @return products in a list
     */
    @GetMapping("/get/{pageIndex}/{column_name}")
    public ResponseEntity get(@PathVariable(value="pageIndex") Integer pageIndex, @PathVariable(value="column_name") String column_name) {
        ArrayList<ProductDTO> products = productService.get(pageIndex, column_name);

        // no products found
        if (products == null || products.isEmpty()) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "No products were found.", "pageIndex: " + pageIndex + "; column_name: " + column_name));
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Return a single product based on id
     * @param id, product ID
     * @return the product
     */
    @GetMapping("/get/{id}")
    public ResponseEntity getSingle(@PathVariable(value="id") Integer id) {
        ArrayList<ProductDTO> products = productService.getSingle(id);

        // no product found
        if (products == null || products.isEmpty()) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "No product was found.", "id: " + id));
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Return a single product with imgs based on id
     * @param id, product ID
     * @return the product with imgs
     */
    @GetMapping("/getDetails/{id}")
    public ResponseEntity getSingleWithImgs(@PathVariable(value="id") Integer id) {
        ProductDTO product = productService.getSingleWithImgs(id);

        // no product found
        if (product == null) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "No product was found.", "id: " + id));
        }

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    /**
     * Return a single image from a product
     * @param id, product ID, string filename
     * @return the product with imgs
     */
    @GetMapping("/getImage/{id}/{filename}")
    public ResponseEntity getSingleImage(@PathVariable(value="id") Integer id, @PathVariable(value="filename") String fileName) {
        ImageDTO image = productService.getImageFromProduct(id, fileName);

        if (image == null) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "No image was found.", "id: " + id + " " + "filename: " + fileName));
        }

        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    /**
     * Update a given product (by ID), enabling/disabling the item
     * @param id, ID of the product to update
     * @param request, the new boolean
     * @return HTTP status response only
     */
    @PostMapping("/setBlock/{id}")
    public ResponseEntity setBlock(@PathVariable(value="id") Integer id, @RequestBody ProductDTO request) {
        ProductDTO product = productService.setEnabled(id, request.getEnabled());

        // null if the ID did not map to an existing category
        if (product == null) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "The product to block was not found.", "id: " + id + "; new block status: " + request.getEnabled()));
        }

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    /**
     * Disable all products by vendor ID
     * Also resets vendorId to 0.
     * @param id, ID of the product to update
     * @return HTTP status response only
     */
    @PostMapping("/setBlockByVendor/{id}")
    public ResponseEntity setBlockByVendor(@PathVariable(value="id") Integer id) {
        try {
            productService.disableByVendorId(id);
        }
        // this ID didn't match any vendors
        catch (EmptyResultDataAccessException e) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "The vendor to block products of was not found.", "id: " + id + "; exception msg: " + e.getLocalizedMessage()));
        }

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    /**
     * Disable all products by vendor ID
     * Also resets vendorId to 0.
     * @param id, ID of the product to update
     * @return HTTP status response only
     */
    @PostMapping("/setBlockByCategory/{id}")
    public ResponseEntity setBlockByCategory(@PathVariable(value="id") Integer id) {
        try {
            productService.disableByCategoryId(id);
        }
        // this ID didn't match any categories
        catch (EmptyResultDataAccessException e) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "The category to block products of was not found.", "id: " + id + "; exception msg: " + e.getLocalizedMessage()));
        }

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    /**
     * Return a list of products in the system filtered by a given search string on ID or name
     * @param search, string to filter returned list on by ID or name
     * @param pageIndex, index of the page of results to return (starts at 0)
     * @return the filtered products in a list
     */
    @GetMapping("/search/{search}/{pageIndex}")
    public ResponseEntity getFiltered(@PathVariable(value="search") String search, @PathVariable(value="pageIndex") Integer pageIndex) {
        ArrayList<ProductDTO> products = productService.getFiltered(search, pageIndex);

        // no products found
        if (products == null || products.isEmpty()) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "No products were found.", "search: " + search));
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Delete a product based on a given ID
     * @param id, ID of the product to delete
     * @return HTTP status response only
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProduct(@PathVariable(value="id") Integer id) {
        try {
            productService.deleteById(id);
        }
        // this ID didn't match any products
        catch (EmptyResultDataAccessException e) {
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND,
                    "No product was found.",
                    "id: " + id + "; exception msg: " + e.getLocalizedMessage()));
        }

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    /**
     * Add a new products based on a given DTO resource
     * @param newProduct, the new product to store in the database
     * @return the newly created product
     */
    @PutMapping("/add")
    public ResponseEntity addProduct(@RequestBody ProductDTO newProduct) {
        ProductDTO created;
        try {
            created = productService.add(newProduct);
        }
        // saving image to file failed (TODO: or something else?)
        catch (NullPointerException e) {
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST,
                    "A null pointer exception occurred while writing image to file.",
                    e.getLocalizedMessage()));
        }

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Get all products that are in a given category, paginated and sorted
     * @param catId, ID of the category to filter by
     * @param pageIndex, index of the page of results to fetch
     * @param column_name, name of the product field we are sorting by (defaults to product ID)
     * @return list of products in the given category
     */
    @GetMapping("/bycategory/{catId}/{pageIndex}/{column_name}")
    public ResponseEntity getByCategory(@PathVariable(value="catId") Integer catId,
                                                          @PathVariable(value="pageIndex") Integer pageIndex,
                                                          @PathVariable(value="column_name") String column_name) {
        ArrayList<ProductDTO> products = productService.getByCategory(catId, pageIndex, column_name);

        if (products == null || products.isEmpty()) {
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST,
                    "No products were found.",
                    "catId: " + catId + "; pageIndex: " + pageIndex + "; column_name: " + column_name));
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * View a detailed product,edit and saved in the database
     * @param request
     * @return
     */
    @PostMapping("/edit/{id}")
    public ResponseEntity edit(@RequestBody ProductDTO request) {
        ProductDTO product = productService.edit(request);
        // null if the ID did not map to an existing product
        if (product == null) {
            // null if the ID did not map to an existing product
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST, "Edit product Failed.",
                    "name: " + request.getName()
                            + "price: " + request.getPrice() +
                            "desc: " + request.getDesc() +
                            "categoryId: " + request.getCategoryId()));
        }

        return new ResponseEntity(product, HttpStatus.OK);
    }

    @PostMapping("/editInventory")
    public ResponseEntity editInventory(@RequestBody ProductInventoryDTO request){
        ProductInventoryDTO product = productService.editInventory(request);

        if(product== null){
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST, "Edit product Failed.",
                    "name: " + request.getName()
                            + "price: " + request.getPrice() +
                            "stock: " + request.getStock() +
                            "buffer: " + request.getBuffer() +
                            "req: " + request.getReq() +
                            "pending: " + request.getPending()
            ));
        }

        return new ResponseEntity(product, HttpStatus.OK);
    }

    @GetMapping("/getInventory/{pageIndex}/{vendorId}")
    public ResponseEntity getInventory(@PathVariable Integer pageIndex, @PathVariable Integer vendorId){
        ArrayList<ProductInventoryDTO> products = productService.getInventory(pageIndex,vendorId);

        if (products == null || products.isEmpty()){
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "No inventory was found.", "vendor id: " + vendorId));
        }


        return new ResponseEntity(products, HttpStatus.OK);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<String>("Hello!", HttpStatus.OK);
    }
}
