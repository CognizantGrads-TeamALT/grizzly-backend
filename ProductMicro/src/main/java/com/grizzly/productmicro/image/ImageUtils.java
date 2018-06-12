package com.grizzly.productmicro.image;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import javax.imageio.ImageIO;

public class ImageUtils {
    public static String readFromFile(Integer productId, String imageName) {
        String imageString = null;
        String path = "product_img/" + productId + "/" + imageName;
        File file = new File(path);
        try {
            String ext = imageName.substring(imageName.lastIndexOf(".") + 1);
            BufferedImage img = ImageIO.read(file);
            imageString = encodeToString(img, ext);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return imageString;
    }


    public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();
            imageString = Base64.getEncoder().encodeToString(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }


    public static void writeToFile(String base64Image, Integer productId, String name) {


        // Check If Directory Already Exists Or Not?
        String path = "product_img/" + productId;
        Path dirPathObj = Paths.get(path);
        boolean dirExists = Files.exists(dirPathObj);
        if(dirExists) {
            System.out.println("! Directory Already Exists !");
        } else {
            try {
                // Creating The New Directory Structure
                Files.createDirectories(dirPathObj);
                System.out.println("! New Directory Successfully Created !");
            } catch (IOException ioExceptionObj) {
                System.out.println("Problem occurred While Creating The Directory Structure= " + ioExceptionObj.getMessage());
            }
        }

        //Specify the file path
        String newPath = path + "/" + name;

        try {
            // Converting a Base64 String into Image byte array
            byte[] imageByteArray = Base64.getDecoder().decode(base64Image);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByteArray);
            BufferedImage image = ImageIO.read(bis);
            bis.close();

            String ext = name.substring(name.lastIndexOf(".") + 1);

            //Write to file
            File outputFile = new File(newPath);
            ImageIO.write(image, ext, outputFile);

        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        }
    }
}