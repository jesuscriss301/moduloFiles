package com.carboexco.moduloFiles.controller;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import com.carboexco.moduloFiles.repository.*;
import com.carboexco.moduloFiles.entity.*;

import java.io.IOException;
import java.util.Date;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FileController {


    private final FileService fileService;
    @Autowired
    FileRepository idfotografiaRepository;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping({"/status"})
    public String status(){
        return "ok";
    }

    @PostMapping(value = "/api/files")
    @ResponseStatus(HttpStatus.OK)
    public void handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("ubicacion") String ubicacion,@RequestParam("fecha") String fecha) throws IOException {

        // Crear una instancia de la fábrica de elementos de archivo de disco
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Establecer el tamaño máximo permitido para un archivo (10 MB en este caso)
        factory.setSizeThreshold(1024 * 1024 * 10); // 10 MB en bytes

        // Crear un objeto ServletFileUpload para manejar las solicitudes de carga de archivos
        ServletFileUpload upload = new ServletFileUpload(factory);

        // Establecer el tamaño máximo permitido para una solicitud completa (opcional)
        upload.setSizeMax(1024 * 1024 * 50); // 50 MB en bytes

        String nombreRamdom= (int)(Math.random()*100000000+1)+ "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        idfotografiaRepository.save(new File(nombreRamdom,ubicacion,fecha));
        // Almacenar el archivo en el servidor utilizando el objeto fileService
        fileService.storeFile(file, ubicacion,nombreRamdom);
    }

    @GetMapping("/files/{ubicacion:.+}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename, @PathVariable String ubicacion) {
        Resource file = fileService.loadFile(filename, ubicacion);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/files/view/{ubicacion:.+}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> viewFile(@PathVariable String filename, @PathVariable String ubicacion) {
        Resource file = fileService.loadFile(filename, ubicacion);

        HttpHeaders headers = new HttpHeaders();

        headers.add("content-disposition", "inline; filename="+file.getFilename());
        headers.setContentType(MediaType.parseMediaType("image/jpeg"));

        ResponseEntity<Resource> response = new ResponseEntity<Resource>(
                file, headers, HttpStatus.OK);

        return response;
    }
}