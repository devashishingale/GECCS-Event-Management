package com.geccs.eventmanagement.controller;

import com.geccs.eventmanagement.dto.EventAttendanceRequest;
import com.geccs.eventmanagement.dto.EventAttendanceResponse;
import com.geccs.eventmanagement.service.EventAttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event-attendance")
public class EventAttendanceController {

    private final EventAttendanceService eventAttendanceService;

    public EventAttendanceController(
            EventAttendanceService eventAttendanceService) {

        this.eventAttendanceService =
                eventAttendanceService;
    }

    @PostMapping
    public ResponseEntity<EventAttendanceResponse> markAttendance(
            @RequestBody EventAttendanceRequest request) {

        return ResponseEntity.ok(
                eventAttendanceService.markAttendance(request)
        );
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<EventAttendanceResponse>> getAttendanceByEventId(
            @PathVariable Long eventId) {

        return ResponseEntity.ok(
                eventAttendanceService.getAttendanceByEventId(eventId)
        );
    }
}