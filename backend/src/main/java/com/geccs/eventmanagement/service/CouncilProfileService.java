package com.geccs.eventmanagement.service;

import com.geccs.eventmanagement.dto.CouncilProfileAssignmentRequest;
import com.geccs.eventmanagement.dto.CouncilProfileResponse;
import com.geccs.eventmanagement.entity.CouncilProfile;
import com.geccs.eventmanagement.entity.User;
import com.geccs.eventmanagement.repository.CouncilProfileRepository;
import com.geccs.eventmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CouncilProfileService {

    private final CouncilProfileRepository councilProfileRepository;
    private final UserRepository userRepository;

    public CouncilProfileService(
            CouncilProfileRepository councilProfileRepository,
            UserRepository userRepository) {

        this.councilProfileRepository = councilProfileRepository;
        this.userRepository = userRepository;
    }

    public List<CouncilProfileResponse> getProfilesByCouncilId(UUID councilId) {

        return councilProfileRepository.findByCouncilId(councilId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CouncilProfileResponse assignUserToProfile(
            CouncilProfileAssignmentRequest request) {

        if (request.getCouncilProfileId() == null) {
            throw new IllegalArgumentException(
                    "Council profile ID is required"
            );
        }

        if (request.getUserId() == null) {
            throw new IllegalArgumentException(
                    "User ID is required"
            );
        }

        CouncilProfile profile = councilProfileRepository
                .findById(request.getCouncilProfileId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Council profile not found: "
                                        + request.getCouncilProfileId()
                        )
                );

        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found: "
                                        + request.getUserId()
                        )
                );

        if (!profile.isActive()) {
            throw new IllegalArgumentException(
                    "Council profile is inactive"
            );
        }

        if (profile.getUser() != null) {
            throw new IllegalArgumentException(
                    "Council profile is already assigned to a user"
            );
        }

        if (!user.isEmailVerified()) {
            throw new IllegalArgumentException(
                    "User email is not verified"
            );
        }

        if (!"ACTIVE".equalsIgnoreCase(user.getAccountStatus())) {
            throw new IllegalArgumentException(
                    "User account is not active"
            );
        }

        if (user.getAcademicUnit() == null) {
            throw new IllegalArgumentException(
                    "User is not linked to an academic unit"
            );
        }

        if (profile.getCouncil().getAcademicUnit() != null) {

            if (!Objects.equals(
                    profile.getCouncil()
                            .getAcademicUnit()
                            .getId(),

                    user.getAcademicUnit()
                            .getId()
            )) {
                throw new IllegalArgumentException(
                        "User academic unit does not match the council"
                );
            }
        }

        profile.setUser(user);

        CouncilProfile savedProfile =
                councilProfileRepository.save(profile);

        return convertToResponse(savedProfile);
    }

    private CouncilProfileResponse convertToResponse(
            CouncilProfile profile) {

        CouncilProfileResponse response =
                new CouncilProfileResponse();

        response.setId(profile.getId());
        response.setPosition(profile.getPosition());

        if (profile.getUser() != null) {
            response.setUserId(profile.getUser().getId());
        }

        if (profile.getParentProfile() != null) {
            response.setParentProfileId(
                    profile.getParentProfile().getId()
            );
        }

        response.setTenureStart(profile.getTenureStart());
        response.setTenureEnd(profile.getTenureEnd());
        response.setActive(profile.isActive());

        return response;
    }
}