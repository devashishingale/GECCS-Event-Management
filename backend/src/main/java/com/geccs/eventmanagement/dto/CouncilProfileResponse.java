package com.geccs.eventmanagement.dto;

import java.time.LocalDate;
import java.util.UUID;

public class CouncilProfileResponse {

    private UUID id;
    private String position;
    private Long userId;
    private UUID parentProfileId;
    private LocalDate tenureStart;
    private LocalDate tenureEnd;
    private boolean active;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UUID getParentProfileId() {
        return parentProfileId;
    }

    public void setParentProfileId(UUID parentProfileId) {
        this.parentProfileId = parentProfileId;
    }

    public LocalDate getTenureStart() {
        return tenureStart;
    }

    public void setTenureStart(LocalDate tenureStart) {
        this.tenureStart = tenureStart;
    }

    public LocalDate getTenureEnd() {
        return tenureEnd;
    }

    public void setTenureEnd(LocalDate tenureEnd) {
        this.tenureEnd = tenureEnd;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}