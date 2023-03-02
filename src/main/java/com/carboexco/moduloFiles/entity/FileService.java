package com.carboexco.moduloFiles.entity;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileService {

    private static final String FILE_DIRECTORY = "/img";

    private final Path rootLocation = Paths.get(FILE_DIRECTORY);

    public void storeFile(MultipartFile file, String ubicacion,String nombre) throws IOException {
        Path filePath = Paths.get(FILE_DIRECTORY + "/" + ubicacion + "/" + nombre);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public Resource loadFile(String filename, String ubicacion) {
        try {
            Path file = rootLocation.resolve(ubicacion+"/"+filename);
            System.out.println(file.toString());
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("FAIL!");
        }
    }
}