package com.geccs.eventmanagement.controller;

import com.geccs.eventmanagement.dto.EventCreateRequest;
import com.geccs.eventmanagement.dto.EventResponse;
import com.geccs.eventmanagement.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {

        return ResponseEntity.ok(
                eventService.getAllEvents()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                eventService.getEventById(id)
        );
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(
            @RequestBody EventCreateRequest request) {

        return ResponseEntity.ok(
                eventService.createEvent(request)
        );
    }
}