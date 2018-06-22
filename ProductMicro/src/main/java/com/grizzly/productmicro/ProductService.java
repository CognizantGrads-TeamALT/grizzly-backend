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
import java.security.NoSuchAlgorithmException;
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

    // Convert a Product object into a ProductDTO
    public ProductDTO productToDTO(Product product) {
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

        return productDTO;
    }

    public ProductInventoryDTO productToInventoryDTO(Product product){
        ProductInventoryDTO pIDTO = new ProductInventoryDTO(product.getName(), product.getStock(), product.getReq(),
                product.getBuffer(), product.getPending(), product.getPrice(), product.getRating());
        pIDTO.setProductId(product.getProductId());

        return pIDTO;
    }
    //(String name, Integer stock, Integer req, Integer buffer, Integer pending, Integer price, Integer rating)

    public ArrayList<ProductDTO> get(Integer pageIndex, String column_name) {
        PageRequest request = getPageRequest(pageIndex, column_name, "product", 25);

        Page<Product> products = productRepository.findAll(request);

        ArrayList<ProductDTO> result = new ArrayList<>();
        for (Product product : products) {
            result.add(productToDTO(product));
        }

        return result;
    }

    public ArrayList<ProductInventoryDTO> getInventory(Integer pageIndex, Integer vendorId){
        PageRequest request = getPageRequest(pageIndex, "default", "product", 25);

        List<Product> products = productRepository.findByVendorId(vendorId, request);

        ArrayList<ProductInventoryDTO> result = new ArrayList<>();
        for(Product product: products){
            result.add(productToInventoryDTO(product));
        }

        return result;
    }

    /**
     * Get a single item from product id.
     * @param productId, the string to match to ID to filter the product by
     * @return ArrayList of Products with Imgs
     */
    public ProductDTO getSingleWithImgs(Integer productId) {
        ProductDTO product = getSingle(productId).get(0);

        return product;
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
    public ArrayList<ProductDTO> getSingle(Integer search) {
        ArrayList<ProductDTO> result = new ArrayList<>();

        ProductDTO product = productToDTO(productRepository.findByProductId(search).get(0));

        result.add(product);

        return result;
    }

    /**
     * Get a filtered list of products, based on a given search string to match to name or ID.
     * @param search, the string to match to name or ID to filter the products by
     * @param pageIndex, the index of the page of results to return (starts at 0)
     * @return ArrayList of Product objs whose names or IDs
     */
    public ArrayList<ProductDTO> getFiltered(String search, Integer pageIndex) {
        try {
            Integer productId = Integer.parseInt(search);

            return getSingle(productId);
        } catch(NumberFormatException e) {
            PageRequest request = getPageRequest(pageIndex, "productId", "product", 30);

            List<Product> products = productRepository.findByProductName(search, request);

            ArrayList<ProductDTO> productsResult = new ArrayList<>();
            for (Product product : products) {
                productsResult.add(productToDTO(product));
            }

            return productsResult;
        }
    }

    /**
     * Add a new product to the database
     * @param newProduct, the entity of the new product to save
     * @return the added product object
     */
    public ProductDTO add(ProductDTO newProduct) {
        Product created = productRepository.save(newProduct.toEntity());

        ImageDTO[] imageDTO = newProduct.getImageDTO();
        for (int i = 0; i < imageDTO.length; i++) {
            saveImageDTO(imageDTO[i], created.getProductId());
        }
        try {
            URL url = new URL("http://alt.ausgrads.academy:8765/categorymicro" +
                                "category/updateCount/" + created.getCategoryId());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e){
            return null;
        }

        ProductDTO productDTO = new ProductDTO(created.getName(), created.getVendorId(), created.getCategoryId(),
                created.getDesc(), created.getPrice(), created.getRating(), created.getEnabled(), imageDTO);
        productDTO.setProductId(created.getProductId());

        return productDTO;
    }

    /**
     * Update an existing product (based on a given ID)
     * @param id, ID of the product to update
     * @param newBool, new status enabled/disabled of product
     */
    public ProductDTO setEnabled(Integer id, Boolean newBool) {
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
        return productToDTO(product);
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
    public ArrayList<ProductDTO> getByCategory(Integer catId, Integer pageIndex, String column_name) {
        List<Product> products = productRepository.findByCategoryId(catId, getPageRequest(pageIndex, column_name, "product", 25));

        ArrayList<ProductDTO> productsResult = new ArrayList<>();
        for (Product product : products) {
            productsResult.add(productToDTO(product));
        }

        return productsResult;
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
    public ProductDTO edit(ProductDTO request) {
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

        // find the existing images
        List<Image> images = imageRepository.findByProductId(prod.getProductId());

        // check if any changes to images are required
        if (request.getImageDTO().length != images.size()) {
            List<ImageDTO> toAdd = new ArrayList<ImageDTO>();
            List<Image> toDel = new ArrayList<Image>();
            // if there isn't a DB image for a DTO image, the DTO one must be added
            for (ImageDTO imgDto : request.getImageDTO()) {
                toAdd.add(imgDto);
            }

            // if there isn't a DTO image for a DB image, the DB one must be deleted
            for (Image img : images) {
                toDel.add(img);
            }

            // perform the adds
            for (ImageDTO add : toAdd) {
                saveImageDTO(add, request.getProductId());
            }

            // perform the deletes
            for (Image del : toDel) {
                ImageUtils.deleteImage(del.getImage_id(), del.getImage_url());
            }
        }

        return productToDTO(prod);
    }

    /**
     * writes a given image DTO to file and saves it as an image in the Database
     * @param imgDto, the image dto to save, as it came in from the front-end
     * @param prodId, the ID of the product to attach the image to
     */
    private void saveImageDTO(ImageDTO imgDto, Integer prodId) {
        String ogName = imgDto.getImgName();
        String content = imgDto.getBase64Image();

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch(NoSuchAlgorithmException e) {
            return; // TODO: handle this better
        }
        md.update(content.getBytes());
        byte[] digest = md.digest();
        String newName = DatatypeConverter
                .printHexBinary(digest).toUpperCase();

        newName += "." + ogName.substring(ogName.lastIndexOf(".") + 1);

        ImageUtils.writeToFile(content, prodId, newName);
        imageRepository.save(new Image(prodId, newName));
    }

    public ProductInventoryDTO editInventory(ProductInventoryDTO request){
        Product prod;
        try{
            prod = productRepository.findByProductId(request.getProductId()).get(0);
        }
        catch (NoSuchElementException e){
            return null;
        }
        prod.setName(request.getName());
        prod.setStock(request.getStock());
        prod.setBuffer(request.getBuffer());
        prod.setPending(request.getPending());
        prod.setPrice(request.getPrice());
        int req = request.getBuffer() - request.getStock();
        if(req < 0) req =0;
        prod.setReq(req);

        productRepository.save(prod);
        return productToInventoryDTO(prod);
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