package com.carboexco.moduloFiles.controller;

import com.carboexco.moduloFiles.entity.File;
import com.carboexco.moduloFiles.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/data/files")
public class IdfotografiaController {

    @Autowired
    FileRepository idfotografiaRepository;

    @GetMapping
    public List<File> getIdfotografiaAll() {
        return idfotografiaRepository.findAll();
    }


    @GetMapping("/direccion/{id}")
    public String getdireccionServidor(@PathVariable int id) {

        Optional<File> idfotografia = idfotografiaRepository.findById(id);

        if (idfotografia.isPresent()) {
            File n= idfotografia.get();
            return "c:/img/"+n.getDireccionCarpeta()+"/"+n.getId()+n.getNombreArchivo();
        }
        return null;
    }
    @GetMapping("/{id}")
    public File getIdfotografiabyId(@PathVariable int id) {

        Optional<File> idfotografia = idfotografiaRepository.findById(id);

        if (idfotografia.isPresent()) {
            return idfotografia.get();
        }
        return null;
    }

    @PostMapping
    public File postIdfotografia(@RequestBody File idfotografia) {
        idfotografiaRepository.save(idfotografia);
        return idfotografia;
    }

    @PutMapping("/{id}")
    public File putIdfotografiabyId(@PathVariable int id, @RequestBody File idfotografia) {

        Optional<File> idfotografiaCurrent = idfotografiaRepository.findById(id);

        if (idfotografiaCurrent.isPresent()) {
            File idfotografiaReturn = idfotografiaCurrent.get();

            idfotografiaReturn.setNombreArchivo(idfotografia.getNombreArchivo());
            idfotografiaReturn.setDireccionCarpeta(idfotografia.getDireccionCarpeta());
            idfotografiaReturn.setFecha(idfotografia.getFecha());

            idfotografiaRepository.save(idfotografiaReturn);
            return idfotografiaReturn;
        }

        return null;
    }

    @DeleteMapping("/{id}")
    public File deleteIdfotografiabyId(@PathVariable int id) {

        Optional<File> idfotografia = idfotografiaRepository.findById(id);

        if (idfotografia.isPresent()) {
            File idfotografiaReturn = idfotografia.get();
            idfotografiaRepository.deleteById(id);
            return idfotografiaReturn;
        }

        return null;
    }
}

