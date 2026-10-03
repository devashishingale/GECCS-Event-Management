package com.geccs.eventmanagement.repository;

import com.geccs.eventmanagement.entity.CouncilProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CouncilProfileRepository extends JpaRepository<CouncilProfile, UUID> {

    List<CouncilProfile> findByCouncilId(UUID councilId);

    List<CouncilProfile> findByUserId(Long userId);

    List<CouncilProfile> findByPosition(String position);
}