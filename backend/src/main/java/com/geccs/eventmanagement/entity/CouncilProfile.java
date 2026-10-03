package com.geccs.eventmanagement.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "council_profiles")
public class CouncilProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "council_id", nullable = false)
    private Council council;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_profile_id")
    private CouncilProfile parentProfile;

    @Column(name = "tenure_start")
    private LocalDate tenureStart;

    @Column(name = "tenure_end")
    private LocalDate tenureEnd;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Council getCouncil() {
        return council;
    }

    public void setCouncil(Council council) {
        this.council = council;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public CouncilProfile getParentProfile() {
        return parentProfile;
    }

    public void setParentProfile(CouncilProfile parentProfile) {
        this.parentProfile = parentProfile;
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