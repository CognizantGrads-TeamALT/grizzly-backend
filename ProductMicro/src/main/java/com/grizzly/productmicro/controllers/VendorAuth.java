package com.grizzly.productmicro.controllers;

import com.grizzly.grizlibrary.errorhandling.ApiError;
import com.grizzly.productmicro.ProductInventoryDTO;
import com.grizzly.productmicro.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.grizzly.grizlibrary.helpers.Helper.buildResponse;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class VendorAuth {
    @Autowired
    private ProductService productService;

    private Boolean hasAccess(String userData) {
        try {
            JSONObject jsonObject = new JSONObject(userData);
            return jsonObject.get("role").equals("vendor");
        } catch (Exception e) {
            System.out.println("oh snap.");
        }

        return false;
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
        if (!hasAccess(userData))
            return buildResponse(new ApiError(HttpStatus.FORBIDDEN, "You do not have access.", "You do not have the proper clearance."));

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

        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
