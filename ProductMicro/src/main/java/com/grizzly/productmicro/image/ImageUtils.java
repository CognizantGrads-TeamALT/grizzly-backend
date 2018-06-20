package com.grizzly.productmicro.image;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ImageUtils {
    private static String deploymentPath = "/opt/deployed/product_img/";

    public static  void deleteImage(Integer productId, String imageName) {
        String path = deploymentPath + productId + "/" + imageName;
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
    }

    public static String readFromFile(Integer productId, String imageName) {
        String imageString = null;
        String path = deploymentPath + productId + "/" + imageName;
        File file = new File(path);

        try {
            FileInputStream imageInFile = new FileInputStream(file);
            byte imageData[] = new byte[(int) file.length()];
            imageInFile.read(imageData);

            // Decompress
            ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
            GZIPInputStream gis = new GZIPInputStream(bis);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }

            imageString = Base64.getEncoder().encodeToString(bos.toByteArray());

            bos.close();
            gis.close();
            bis.close();
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
            if(dirExists) {
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
            byte[] imageByteArray = Base64.getDecoder().decode(base64Image);

            Path dirPathObj = Paths.get(newPath);

            Files.createFile(dirPathObj, PosixFilePermissions.asFileAttribute(fullPermission));
            Files.setPosixFilePermissions(dirPathObj, fullPermission);

            // Compression.
            ByteArrayOutputStream obj = new ByteArrayOutputStream();
            GZIPOutputStream gis = new GZIPOutputStream(obj);

            gis.write(imageByteArray);
            gis.flush();
            gis.close();

            Files.write(dirPathObj, obj.toByteArray());
        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}