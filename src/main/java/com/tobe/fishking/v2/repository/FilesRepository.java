package com.tobe.fishking.v2.repository;

import com.tobe.fishking.v2.entity.fishing.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FilesRepository extends JpaRepository<Ship, Long> {
    Optional<Ship> findById(Long Id);
}

