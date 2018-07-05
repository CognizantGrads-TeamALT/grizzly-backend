package com.grizzly.productmicro.controllers;

import com.grizzly.grizlibrary.errorhandling.ApiError;
import com.grizzly.productmicro.model.ProductDTO;
import com.grizzly.productmicro.model.ProductInventoryDTO;
import com.grizzly.productmicro.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import static com.grizzly.grizlibrary.helpers.Helper.buildResponse;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class VendorAuth {
    @Autowired
    private ProductService productService;

    private Integer hasAccess(String userData) {
        try {
            JSONObject jsonObject = new JSONObject(userData);

            if (jsonObject.get("role").equals("admin"))
                return -1;
            else if (jsonObject.get("role").equals("vendor"))
                return (Integer) jsonObject.get("vendorId");
        } catch (Exception e) {
            return 0;
        }

        return 0;
    }

    /**
     * Edit product inventory
     * @param request
     * @return
     *
     * AUTHENTICATION (vendor only)
     */
    @PostMapping("/editInventory")
    public ResponseEntity editInventory(@RequestBody ProductInventoryDTO request, @RequestHeader(value="User-Data") String userData){
        // Authentication
        Integer accessLevel = hasAccess(userData);
        if (accessLevel <= 0)
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));
        else {
            ProductDTO productInfo = productService.getSingleById(request.getProductId());
            if (productInfo != null) {
                if (productInfo.getVendorId() != accessLevel)
                    return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "This is not your product."));
            }
        }

        ProductInventoryDTO product = productService.editInventory(request);

        if (product == null) {
            return buildResponse(new ApiError(HttpStatus.BAD_REQUEST, "Edit product Failed.",
                    "name: " + request.getName()
                            + "price: " + request.getPrice() +
                            "stock: " + request.getStock() +
                            "buffer: " + request.getBuffer() +
                            "req: " + request.getReq() +
                            "pending: " + request.getPending()
            ));
        }

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    /**
     * getting product inventory
     * @param pageIndex
     * @return
     *
     * AUTHENTICATION (vendor only)
     */
    @GetMapping("/getInventory/{pageIndex}")
    public ResponseEntity getInventory(@PathVariable Integer pageIndex, @RequestHeader(value="User-Data") String userData){
        // Authentication
        Integer accessLevel = hasAccess(userData);
        if (accessLevel <= 0)
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

        ArrayList<ProductInventoryDTO> products = productService.getInventory(pageIndex, accessLevel);

        if (products == null || products.isEmpty()){
            return buildResponse(new ApiError(HttpStatus.NOT_FOUND, "No inventory was found.", "vendor id: " + accessLevel));
        }


        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
