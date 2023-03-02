package com.carboexco.moduloFiles.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "file")
@NoArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nombre_archivo", nullable = false)
    private String nombreArchivo;

    @Lob
    @Column(name = "direccionCarpeta", nullable = false)
    private String direccionCarpeta;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    public File(String nombreArchivo, String direccionCarpeta, String fecha) {
        this.nombreArchivo = nombreArchivo;
        this.direccionCarpeta = direccionCarpeta;
        this.fecha = LocalDate.parse(fecha);
    }
}