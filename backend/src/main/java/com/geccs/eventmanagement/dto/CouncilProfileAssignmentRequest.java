package com.geccs.eventmanagement.dto;

import java.util.UUID;

public class CouncilProfileAssignmentRequest {

    private UUID councilProfileId;

    private Long userId;

    public UUID getCouncilProfileId() {
        return councilProfileId;
    }

    public void setCouncilProfileId(UUID councilProfileId) {
        this.councilProfileId = councilProfileId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}