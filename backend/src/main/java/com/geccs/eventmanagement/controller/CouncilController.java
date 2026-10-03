package com.geccs.eventmanagement.controller;

import com.geccs.eventmanagement.dto.CouncilResponse;
import com.geccs.eventmanagement.service.CouncilService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/councils")
public class CouncilController {

    private final CouncilService councilService;

    public CouncilController(CouncilService councilService) {
        this.councilService = councilService;
    }

    @GetMapping
    public ResponseEntity<List<CouncilResponse>> getAllCouncils() {
        return ResponseEntity.ok(councilService.getAllCouncils());
    }

    @GetMapping("/{code}")
    public ResponseEntity<CouncilResponse> getCouncilByCode(
            @PathVariable String code) {

        return ResponseEntity.ok(
                councilService.getCouncilByCode(code)
        );
    }
}