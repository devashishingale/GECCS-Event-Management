package com.geccs.eventmanagement.controller;

import com.geccs.eventmanagement.dto.CouncilProfileAssignmentRequest;
import com.geccs.eventmanagement.dto.CouncilProfileResponse;
import com.geccs.eventmanagement.service.CouncilProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/council-profiles")
public class CouncilProfileController {

    private final CouncilProfileService councilProfileService;

    public CouncilProfileController(
            CouncilProfileService councilProfileService) {
        this.councilProfileService = councilProfileService;
    }

    @GetMapping("/council/{councilId}")
    public ResponseEntity<List<CouncilProfileResponse>> getProfilesByCouncilId(
            @PathVariable UUID councilId) {

        return ResponseEntity.ok(
                councilProfileService.getProfilesByCouncilId(councilId)
        );
    }

    @PostMapping("/assign")
    public ResponseEntity<CouncilProfileResponse> assignUserToProfile(
            @RequestBody CouncilProfileAssignmentRequest request) {

        return ResponseEntity.ok(
                councilProfileService.assignUserToProfile(request)
        );
    }
}