package com.grizzly.productmicro;

import com.grizzly.productmicro.image.Image;
import com.grizzly.productmicro.image.ImageDTO;
import com.grizzly.productmicro.image.ImageRepository;
import com.grizzly.productmicro.image.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;

    public ArrayList<Product> get(Integer pageIndex, String column_name) {
        PageRequest request = getPageRequest(pageIndex, column_name);
        return makeListFromIterable(productRepository.findAll(request));
    }

    /**
    * Utility function to generate a pagerequest to tell the database how to page and sort a query
    * @param column_name, the fieldname in the database to sort the list
    * @return pageRequest to the method called
    */
    public PageRequest getPageRequest(Integer pageIndex, String column_name) {
        final String[] fields = {"productId", "name", "vendorId", "categoryId", "desc", "price", "enabled"};
        String sortField;
        if (Arrays.asList(fields).contains(column_name)) {
            sortField = column_name;
        } else {
            sortField = "productId";
        }

        Sort sort = new Sort(Sort.Direction.ASC, sortField);

        PageRequest request = PageRequest.of(pageIndex, 25, sort);
        return request;
    }

    /**
     * Get a single item from product id.
     * @param productId, the string to match to ID to filter the product by
     * @return ArrayList of Products with Imgs
     */
    public ArrayList<ProductDTO> getSingleWithImgs(Integer productId) {
        ArrayList<Product> found = getSingle(productId);
        Product product = found.get(0);
        Integer pId = product.getProductId();
        List<Image> images = imageRepository.findByProductId(pId);
        ImageDTO[] imageDTO = new ImageDTO[images.size()];
        for (int i =0; i < images.size(); i++)
        {
            String imgName = images.get(i).getImage_url();
            String base64Image = ImageUtils.readFromFile(pId, imgName );
            ImageDTO image = new ImageDTO();
            image.setImgName(imgName);
            String base64String = "data:image/" + imgName.substring(imgName.lastIndexOf(".") + 1) + ";name="
                + imgName + ";base64," + base64Image;
            image.setBase64Image(base64String);
            imageDTO[i] = image;
        }
        ProductDTO productDTO = new ProductDTO(product.getName(), product.getVendorId(), product.getCategoryId(),
                product.getDesc(), product.getPrice(), product.getEnabled(), imageDTO);
        productDTO.setProductId(product.getProductId());
        ArrayList<ProductDTO> productDTOArrayList = new ArrayList<>();
        productDTOArrayList.add(productDTO);
        return productDTOArrayList;
    }

    /**
     * Get a single item from product id.
     * @param search, the string to match to ID to filter the product by
     * @return ArrayList of Product objs whose names or IDs
     */
    public ArrayList<Product> getSingle(Integer search) {
        Sort sort = new Sort(Sort.Direction.ASC, "productId");
        PageRequest request = PageRequest.of(0, 25, sort);

        return makeListFromIterable(
                productRepository.findByProductId(search, request)
        );
    }

    /**
     * Get a filtered list of products, based on a given search string to match to name or ID.
     * @param search, the string to match to name or ID to filter the products by
     * @return ArrayList of Product objs whose names or IDs
     */
    public ArrayList<Product> getFiltered(String search) {
        try {
            Integer productId = Integer.parseInt(search);

            return getSingle(productId);
        } catch(NumberFormatException e) {
            System.out.println("High..." + search);
            Sort sort = new Sort(Sort.Direction.ASC, "productId");
            PageRequest request = PageRequest.of(0, 25, sort);

            return makeListFromIterable(
                    productRepository.findByProductName(search, request)
            );
        }
    }

    /**
     * Add a new product to the database
     * @param newProduct, the entity of the new product to save
     * @return the added product object
     */
    public Product add(ProductDTO newProduct) {
        Product created = productRepository.save(newProduct.toEntity());
        for (int i =0; i < newProduct.getImageDTO().length; i++)
        {
            String name = newProduct.getImageDTO()[i].getImgName();
            ImageUtils.writeToFile(newProduct.getImageDTO()[i].getBase64Image(), created.getProductId(), name );
            imageRepository.save(new Image(created.getProductId(),name ));
        }
        try {
            URL url = new URL("http://alt.ausgrads.academy:8765/categorymicro" +
                                "category/updateCount/" + created.getCategoryId());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            int status = con.getResponseCode();
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e){
            return null;
        }

        return created;
    }

    /**
     * Update an existing product (based on a given ID)
     * @param id, ID of the product to update
     * @param newBool, new status enabled/disabled of product
     */
    public Product setEnabled(Integer id, Boolean newBool) {
        // find the existing product
        Product product;
        try {
            product = productRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            return null;
        }

        // make changes
        product.setEnabled(newBool);

        // save the updated product
        productRepository.save(product);
        return product;
    }

    /**
     * Delete a product given an ID
     * @param deleteId, ID of the product to delete
     */
    public void deleteById(Integer deleteId) {
        productRepository.deleteById(deleteId);
    }

    /**
     * Get all products (given pagination and sorting) for a given category
     * @param catId, ID of the category to filter by
     * @param pageIndex, index of the page of results to retrieve from the database
     * @param column_name, name of the product field to sort the results by
     * @return list of products in the category
     */
    public ArrayList<Product> getByCategory(Integer catId, Integer pageIndex, String column_name) {
        return makeListFromIterable(productRepository.findByCategoryId(catId, getPageRequest(pageIndex, column_name)));
    }

    /**
     * Make an ArrayList of Objects based on a passed-in Iterable
     * @param iter An Iterable of Objects
     * @return An ArrayList made from the Iterable
     */
    public static <T> ArrayList<T> makeListFromIterable(Iterable<T> iter) {
        ArrayList<T> list = new ArrayList<T>();

        for(T item: iter) {
            list.add(item);
        }

        return list;
    }
}