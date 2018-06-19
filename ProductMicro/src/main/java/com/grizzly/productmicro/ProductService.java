package com.grizzly.productmicro;

import com.grizzly.productmicro.image.Image;
import com.grizzly.productmicro.image.ImageDTO;
import com.grizzly.productmicro.image.ImageRepository;
import com.grizzly.productmicro.image.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static com.grizzly.grizlibrary.helpers.Helper.makeListFromIterable;
import static com.grizzly.grizlibrary.helpers.Helper.getPageRequest;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;

    public ArrayList<ProductDTO> get(Integer pageIndex, String column_name) {
        PageRequest request = getPageRequest(pageIndex, column_name, "product", 25);

        Page<Product> products = productRepository.findAll(request);

        ArrayList<ProductDTO> result = new ArrayList<>();
        for (Product product : products) {
            List<Image> images = imageRepository.findByProductId(product.getProductId());

            ImageDTO[] imageDTO = new ImageDTO[images.size()];

            for (int i = 0; i < images.size(); i++) {
                String imgName = images.get(i).getImage_url();

                ImageDTO image = new ImageDTO();
                image.setImgName(imgName);

                imageDTO[i] = image;
            }

            ProductDTO productDTO = new ProductDTO(product.getName(), product.getVendorId(), product.getCategoryId(),
                    product.getDesc(), product.getPrice(), product.getRating(), product.getEnabled(), imageDTO);
            productDTO.setProductId(product.getProductId());

            result.add(productDTO);
        }

        return result;
    }

    /**
     * Get a single item from product id.
     * @param productId, the string to match to ID to filter the product by
     * @return ArrayList of Products with Imgs
     */
    public ProductDTO getSingleWithImgs(Integer productId) {
        Product product = getSingle(productId).get(0);

        List<Image> images = imageRepository.findByProductId(productId);

        ImageDTO[] imageDTO = new ImageDTO[images.size()];

        for (int i = 0; i < images.size(); i++) {
            String imgName = images.get(i).getImage_url();

            ImageDTO image = new ImageDTO();
            image.setImgName(imgName);

            imageDTO[i] = image;
        }

        ProductDTO productDTO = new ProductDTO(product.getName(), product.getVendorId(), product.getCategoryId(),
                product.getDesc(), product.getPrice(), product.getRating(), product.getEnabled(), imageDTO);
        productDTO.setProductId(product.getProductId());

        return productDTO;
    }

    public ImageDTO getImageFromProduct(Integer productId, String fileName) {
        Image image = imageRepository.findByProductIdAndName(productId, fileName);

        String imageName = image.getImage_url();
        String base64Image = ImageUtils.readFromFile(productId, imageName);

        ImageDTO response = new ImageDTO();
        response.setImgName(image.getImage_url());

        String base64String = "data:image/" + imageName.substring(imageName.lastIndexOf(".") + 1)
                + ";base64," + base64Image;
        response.setBase64Image(base64String);

        return response;
    }

    /**
     * Get a single item from product id.
     * @param search, the string to match to ID to filter the product by
     * @return ArrayList of Product objs whose names or IDs
     */
    public ArrayList<Product> getSingle(Integer search) {
        return makeListFromIterable(
                productRepository.findByProductId(search)
        );
    }

    /**
     * Get a filtered list of products, based on a given search string to match to name or ID.
     * @param search, the string to match to name or ID to filter the products by
     * @param pageIndex, the index of the page of results to return (starts at 0)
     * @return ArrayList of Product objs whose names or IDs
     */
    public ArrayList<Product> getFiltered(String search, Integer pageIndex) {
        try {
            Integer productId = Integer.parseInt(search);

            return getSingle(productId);
        } catch(NumberFormatException e) {
            PageRequest request = getPageRequest(pageIndex, "productId", "product", 30);
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

        ImageDTO[] imageDTO = newProduct.getImageDTO();
        for (int i = 0; i < imageDTO.length; i++) {
            String ogName = imageDTO[i].getImgName();
            String content = imageDTO[i].getBase64Image();

            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(content.getBytes());
                byte[] digest = md.digest();
                String newName = DatatypeConverter
                        .printHexBinary(digest).toUpperCase();

                newName += ogName.substring(ogName.lastIndexOf(".") + 1);

                ImageUtils.writeToFile(content, created.getProductId(), newName);
                imageRepository.save(new Image(created.getProductId(), newName));
            } catch (Exception e) {
                return null;
            }
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

        // Delete imgs
        List<Image> images = imageRepository.findByProductId(deleteId);
        for (Image img : images)
            ImageUtils.deleteImage(deleteId, img.getImage_url());

        imageRepository.deleteAll(images);
    }

    /**
     * Get all products (given pagination and sorting) for a given category
     * @param catId, ID of the category to filter by
     * @param pageIndex, index of the page of results to retrieve from the database
     * @param column_name, name of the product field to sort the results by
     * @return list of products in the category
     */
    public ArrayList<Product> getByCategory(Integer catId, Integer pageIndex, String column_name) {
        return makeListFromIterable(productRepository.findByCategoryId(catId, getPageRequest(pageIndex, column_name, "product", 25)));
    }

    /**
     * Disable all products with the vendorId
     * Set local vendorId to 0
     * @param vendorId, ID of the vendor
     */
    public void disableByVendorId(Integer vendorId) {
        productRepository.disableByVendorId(vendorId);
    }


    /**
     * Update an existing product (based on a given ID) with a new parameters
     * @param  request, productId, categoryId, vendorID, price, imageDTO, rating, enabled of the product to update
     * @return the original product object; null if none was found
     *
     */
    public Product edit(ProductDTO request) {
        // find the existing product
        Product prod;
        try {
            prod = productRepository.findByProductId(request.getProductId()).get(0);
        } catch (NoSuchElementException e) {
            return null;
        }

        // make the changes to the product
        prod.setName(request.getName());
        prod.setDesc(request.getDesc());
        prod.setVendorId(request.getVendorId());
        prod.setPrice(request.getPrice());
        prod.setRating(request.getRating());
        prod.setEnabled(request.getEnabled());

        // save the updated product
      productRepository.save(prod);
        return prod;
    }

    /**
     * Disable all products with the categoryId
     * Set local categoryId to 0
     * @param categoryId, ID of the category
     */
    public void disableByCategoryId(Integer categoryId) {
        productRepository.disableByCategoryId(categoryId);
    }

}