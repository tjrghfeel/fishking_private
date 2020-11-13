package com.tobe.fishking.v2.repository;

import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.fishing.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FilesRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findById(Long Id);
}

