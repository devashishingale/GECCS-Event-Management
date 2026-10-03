package com.geccs.eventmanagement.dto;

import java.util.UUID;

public class CouncilResponse {

    private UUID id;
    private String name;
    private String code;
    private String type;
    private boolean active;
    private String academicUnitName;
    private String academicUnitCode;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getAcademicUnitName() {
        return academicUnitName;
    }

    public void setAcademicUnitName(String academicUnitName) {
        this.academicUnitName = academicUnitName;
    }

    public String getAcademicUnitCode() {
        return academicUnitCode;
    }

    public void setAcademicUnitCode(String academicUnitCode) {
        this.academicUnitCode = academicUnitCode;
    }
}