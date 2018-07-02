package com.grizzly.vendormicro.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

public class ImageUtils {
    private static String deploymentPath = "/opt/deployed/vendor_img/";

    /**
     * Disabled because multiple products may link to the same image.
     * If deleted on one product, it may delete the image used in other products.
     public static void deleteImage(String imageName) {
         String path = deploymentPath + "/" + imageName;
         File file = new File(path);

         //delete if exists
         try {
            boolean success = Files.deleteIfExists(file.toPath());
            System.out.println("Delete status: " + success);
         } catch (IOException | SecurityException e) {
            System.err.println(e);
         } catch (Exception e) {
            e.printStackTrace();
         }
     }*/

    public static String readFromFile(Integer productId, String imageName) {
        String imageString = null;
        String path = deploymentPath + productId + "/" + imageName;
        File file = new File(path);
        try {
            FileInputStream imageInFile = new FileInputStream(file);
            byte imageData[] = new byte[(int) file.length()];
            imageInFile.read(imageData);
            imageString = Base64.getEncoder().encodeToString(imageData);

            imageInFile.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
        return imageString;
    }

    public static void writeToFile(String base64Image, Integer productId, String name) {
        // Set Permission
        Set<PosixFilePermission> fullPermission = new HashSet<>();
        fullPermission.add(PosixFilePermission.OWNER_EXECUTE);
        fullPermission.add(PosixFilePermission.OWNER_READ);
        fullPermission.add(PosixFilePermission.OWNER_WRITE);

        fullPermission.add(PosixFilePermission.GROUP_EXECUTE);
        fullPermission.add(PosixFilePermission.GROUP_READ);
        fullPermission.add(PosixFilePermission.GROUP_WRITE);

        fullPermission.add(PosixFilePermission.OTHERS_EXECUTE);
        fullPermission.add(PosixFilePermission.OTHERS_READ);
        fullPermission.add(PosixFilePermission.OTHERS_WRITE);

        String path = deploymentPath + productId;

        try {
            // Check If Directory Already Exists Or Not?

            Path dirPathObj = Paths.get(path);

            boolean dirExists = Files.exists(dirPathObj);
            if (dirExists) {
                System.out.println("! Directory Already Exists !");
            } else {
                try {
                    // Creating The New Directory Structure
                    Files.createDirectories(dirPathObj, PosixFilePermissions.asFileAttribute(fullPermission));
                    Files.setPosixFilePermissions(dirPathObj, fullPermission);
                    System.out.println("! New Directory Successfully Created !");
                } catch (IOException ioExceptionObj) {
                    System.out.println("Problem occurred While Creating The Directory Structure= " + ioExceptionObj.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Specify the file path
        String newPath = path + "/" + name;
        try {
            // Converting a Base64 String into Image byte array
            String[] split = base64Image.split(","); // strip out the un-needed data section.
            if (split.length < 2) return; // fail.
            byte[] imageByteArray = Base64.getDecoder().decode(split[1]);
            Path dirPathObj = Paths.get(newPath);
            Files.createFile(dirPathObj, PosixFilePermissions.asFileAttribute(fullPermission));
            Files.setPosixFilePermissions(dirPathObj, fullPermission);
            Files.write(dirPathObj, imageByteArray);
        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}