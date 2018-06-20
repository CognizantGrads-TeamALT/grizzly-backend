package com.grizzly.vendormicro;

import com.grizzly.vendormicro.image.Image;
import com.grizzly.vendormicro.image.ImageDTO;
import com.grizzly.vendormicro.image.ImageRepository;
import com.grizzly.vendormicro.image.ImageUtils;
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

import static com.grizzly.grizlibrary.helpers.Helper.getPageRequest;
import static com.grizzly.grizlibrary.helpers.Helper.makeListFromIterable;

@Service
public class VendorService {
    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ImageRepository imageRepository;

    // Convert a Product object into a ProductDTO
    public VendorDTO vendorToDTO(Vendor vendor) {
        List<Image> images = imageRepository.findByVendorId(vendor.getVendorId());

        ImageDTO[] imageDTO = new ImageDTO[images.size()];

        for (int i = 0; i < images.size(); i++) {
            String imgName = images.get(i).getImage_url();

            ImageDTO image = new ImageDTO();
            image.setImgName(imgName);

            imageDTO[i] = image;
        }

        VendorDTO vendorDTO = new VendorDTO(vendor.getName(), vendor.getContactNum(), vendor.getWebsite(), vendor.getEmail(),
                                vendor.getBio(), vendor.getEnabled(), imageDTO);
        vendorDTO.setVendorId(vendor.getVendorId());

        return vendorDTO;
    }

    public ArrayList<VendorDTO> get(Integer pageIndex, String column_name) {
        PageRequest request = getPageRequest(pageIndex, column_name, "vendor", 25);

        Page<Vendor> vendors = vendorRepository.findAll(request);

        ArrayList<VendorDTO> result = new ArrayList<>();
        for (Vendor vendor : vendors) {
            result.add(vendorToDTO(vendor));
        }

        return result;
    }

    /**
     * Get a single item from product id.
     * @param id, the string to match to ID to filter the product by
     * @return ArrayList of Product objs whose names or IDs
     */
    public ArrayList<VendorDTO> getSingle(Integer id) {
        ArrayList<VendorDTO> result = new ArrayList<>();

        VendorDTO vendor = vendorToDTO(vendorRepository.findByVendorId(id).get(0));

        result.add(vendor);

        return result;
    }

    /**
     * Get a filtered list of vendors, based on a given search string to match to name or ID.
     * @param search, the string to match to name or ID to filter the vendors by
     * @return ArrayList of Vendor objs whose names or IDs
     */
    public ArrayList<VendorDTO> getFiltered(String search) {
        try {
            Integer vendorId = Integer.parseInt(search);

            return getSingle(vendorId);
        } catch(NumberFormatException e) {
            PageRequest request = getPageRequest(0, "vendorId", "vendor", 25);

            List<Vendor> vendors = vendorRepository.findByVendorName(search, request);

            ArrayList<VendorDTO> vendorResult = new ArrayList<>();
            for (Vendor vendor : vendors) {
                vendorResult.add(vendorToDTO(vendor));
            }

            return vendorResult;
        }
    }

    /**
     * Update an existing vendor (based on a given ID)
     * @param id, ID of the vendor to update
     * @param newBool, new status enabled/disabled of vendor
     */
    public Vendor setEnabled(Integer id, Boolean newBool) {
        // find the existing vendor
        Vendor vendor;
        try {
            vendor = vendorRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            return null;
        }

        // make changes
        vendor.setEnabled(newBool);

        // save the updated vendor
        vendorRepository.save(vendor);
        return vendor;
    }

    /**
     * Delete a vendor given an ID
     * @param deleteId, ID of the vendor to delete
     */
    public void deleteById(Integer deleteId) {
        vendorRepository.deleteById(deleteId);
    }

    /**
     * Add a new vendor to the database
     * @param newVendor, the entity of the new vendor to save
     * @return the added vendor object
     */
    public VendorDTO add(VendorDTO newVendor) {
        Vendor created = vendorRepository.save(newVendor.toEntity());

        ImageDTO[] imageDTO = newVendor.getImageDTO();
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

                ImageUtils.writeToFile(content, created.getVendorId(), newName);
                imageRepository.save(new Image(created.getVendorId(), newName));
            } catch (Exception e) {
                return null;
            }
        }

        VendorDTO vendorDTO = new VendorDTO(created.getName(), created.getContactNum(), created.getWebsite(), created.getEmail(),
                created.getBio(), created.getEnabled(), imageDTO);
        vendorDTO.setVendorId(created.getVendorId());

        return vendorDTO;
    }

    /**
     * Get a list of vendors based on vendor IDs
     * @param ids, The list of Vendor ids that are to be fetched
     * @return ArrayList of Vendor objs whose IDs match ids
     */
    public ArrayList<VendorDTO> getBatchbyId(List<String> ids) {
        List<Integer> vendorIds = new ArrayList<>();

        for (String id: ids){
            vendorIds.add(Integer.parseInt(id));
        }

        List<Vendor> vendors = vendorRepository.findByVendorIdIn(vendorIds);

        ArrayList<VendorDTO> vendorsResult = new ArrayList<>();

        for (Vendor vendor : vendors) {
            vendorsResult.add(vendorToDTO(vendor));
        }

        return vendorsResult;
    }
}
