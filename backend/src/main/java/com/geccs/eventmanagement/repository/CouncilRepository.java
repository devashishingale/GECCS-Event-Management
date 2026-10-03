package com.geccs.eventmanagement.repository;

import com.geccs.eventmanagement.entity.Council;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CouncilRepository extends JpaRepository<Council, UUID> {

    Optional<Council> findByCode(String code);

    Optional<Council> findByName(String name);
}