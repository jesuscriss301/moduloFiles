package com.carboexco.moduloFiles.controller;

import com.carboexco.moduloFiles.entity.File;
import com.carboexco.moduloFiles.entity.FileService;
import com.carboexco.moduloFiles.repository.FileRepository;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

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
    public int handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("ubicacion") String ubicacion, @RequestParam("nombre") String nombre, @RequestParam("fecha") String fecha) throws IOException {

        // Crear una instancia de la fábrica de elementos de archivo de disco
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Establecer el tamaño máximo permitido para un archivo (10 MB en este caso)
        factory.setSizeThreshold(1024 * 1024 * 10); // 10 MB en bytes

        // Crear un objeto ServletFileUpload para manejar las solicitudes de carga de archivos
        ServletFileUpload upload = new ServletFileUpload(factory);

        // Establecer el tamaño máximo permitido para una solicitud completa (opcional)
        upload.setSizeMax(1024 * 1024 * 50); // 50 MB en bytes

        File archivo = new File(nombre+"."+StringUtils.getFilenameExtension(file.getOriginalFilename()),ubicacion,fecha);
        idfotografiaRepository.save(archivo);
        String str=archivo.getId()+archivo.getNombreArchivo();
        // Almacenar el archivo en el servidor utilizando el objeto fileService
        fileService.storeFile(file, ubicacion,str);
        return archivo.getId();
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable int id) {
        Optional<File> idfotografia = idfotografiaRepository.findById(id);

        if (idfotografia.isPresent()) {
            File data= idfotografia.get();
            Resource file = fileService.loadFile(data.getId()+data.getNombreArchivo(),data.getDireccionCarpeta());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        }
        return null;

    }

    @GetMapping("/files/view/{id}")
    public ResponseEntity<Resource> viewFile(@PathVariable int id) {
        Optional<File> idfotografia = idfotografiaRepository.findById(id);

        if (idfotografia.isPresent()) {
            File data = idfotografia.get();
            Resource file = fileService.loadFile(data.getId() + data.getNombreArchivo(), data.getDireccionCarpeta());

            HttpHeaders headers = new HttpHeaders();

            headers.add("content-disposition", "inline; filename=" + file.getFilename());
            headers.setContentType(MediaType.parseMediaType("image/jpeg"));

            ResponseEntity<Resource> response = new ResponseEntity<Resource>(
                    file, headers, HttpStatus.OK);
            return response;
        }
        return null;
    }

    @GetMapping("/filesPDF/view/{id}")
    public ResponseEntity<InputStreamResource> getPDF(@PathVariable int id) throws IOException {
        Optional<File> idfotografia = idfotografiaRepository.findById(id);

        if (idfotografia.isPresent()) {
            File data = idfotografia.get();
            Resource file = fileService.loadFile(data.getId() + data.getNombreArchivo(), data.getDireccionCarpeta());
            InputStream inputStream = new FileInputStream("C://home/sistemas/c/img/"+data.getDireccionCarpeta()+"/"+data.getId() + data.getNombreArchivo());
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename="+data.getId() + data.getNombreArchivo());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(inputStreamResource);
        }
        return null;
    }
}