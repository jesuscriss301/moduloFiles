package com.carboexco.moduloFiles.repository;

import com.carboexco.moduloFiles.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Integer> {
}