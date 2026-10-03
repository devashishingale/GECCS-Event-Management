package com.geccs.eventmanagement.controller;

import com.geccs.eventmanagement.dto.EventRegistrationRequest;
import com.geccs.eventmanagement.dto.EventRegistrationResponse;
import com.geccs.eventmanagement.service.EventRegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event-registrations")
public class EventRegistrationController {

    private final EventRegistrationService eventRegistrationService;

    public EventRegistrationController(
            EventRegistrationService eventRegistrationService) {

        this.eventRegistrationService =
                eventRegistrationService;
    }

    @PostMapping
    public ResponseEntity<EventRegistrationResponse> registerForEvent(
            @RequestBody EventRegistrationRequest request) {

        return ResponseEntity.ok(
                eventRegistrationService.registerForEvent(request)
        );
    }
}