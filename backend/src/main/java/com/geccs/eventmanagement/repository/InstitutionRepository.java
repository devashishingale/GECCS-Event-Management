package com.geccs.eventmanagement.repository;

import com.geccs.eventmanagement.entity.Institution;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InstitutionRepository
        extends JpaRepository<Institution, UUID> {

    Optional<Institution> findByCode(String code);

    Optional<Institution> findByEmailDomain(String emailDomain);
}