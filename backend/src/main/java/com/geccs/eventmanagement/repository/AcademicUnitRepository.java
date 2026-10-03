package com.geccs.eventmanagement.repository;

import com.geccs.eventmanagement.entity.AcademicUnit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AcademicUnitRepository
        extends JpaRepository<AcademicUnit, UUID> {

    Optional<AcademicUnit> findByCode(String code);
}