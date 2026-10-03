package com.geccs.eventmanagement.service;

import com.geccs.eventmanagement.dto.EventAttendanceRequest;
import com.geccs.eventmanagement.dto.EventAttendanceResponse;
import com.geccs.eventmanagement.entity.Event;
import com.geccs.eventmanagement.entity.EventAttendance;
import com.geccs.eventmanagement.entity.User;
import com.geccs.eventmanagement.repository.EventAttendanceRepository;
import com.geccs.eventmanagement.repository.EventRegistrationRepository;
import com.geccs.eventmanagement.repository.EventRepository;
import com.geccs.eventmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventAttendanceService {

    private final EventAttendanceRepository attendanceRepository;
    private final EventRegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventAttendanceService(
            EventAttendanceRepository attendanceRepository,
            EventRegistrationRepository registrationRepository,
            EventRepository eventRepository,
            UserRepository userRepository) {

        this.attendanceRepository = attendanceRepository;
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public EventAttendanceResponse markAttendance(
            EventAttendanceRequest request) {

        if (request.getEventId() == null) {
            throw new IllegalArgumentException(
                    "Event ID is required"
            );
        }

        if (request.getUserId() == null) {
            throw new IllegalArgumentException(
                    "User ID is required"
            );
        }

        Event event = eventRepository.findById(
                request.getEventId()
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "Event not found: " + request.getEventId()
                )
        );

        User user = userRepository.findById(
                request.getUserId()
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "User not found: " + request.getUserId()
                )
        );

        if (!registrationRepository.existsByEventIdAndUserId(
                event.getId(),
                user.getId())) {

            throw new IllegalArgumentException(
                    "User is not registered for this event"
            );
        }

        if (attendanceRepository.existsByEventIdAndUserId(
                event.getId(),
                user.getId())) {

            throw new IllegalArgumentException(
                    "Attendance has already been marked"
            );
        }

        String status = request.getStatus();

        if (status == null ||
                status.trim().isEmpty()) {

            status = "PRESENT";
        }

        status = status.toUpperCase();

        if (!status.equals("PRESENT") &&
                !status.equals("ABSENT")) {

            throw new IllegalArgumentException(
                    "Attendance status must be PRESENT or ABSENT"
            );
        }

        EventAttendance attendance =
                new EventAttendance();

        attendance.setEvent(event);
        attendance.setUser(user);
        attendance.setStatus(status);

        EventAttendance savedAttendance =
                attendanceRepository.save(attendance);

        return convertToResponse(savedAttendance);
    }

    public List<EventAttendanceResponse> getAttendanceByEventId(
            Long eventId) {

        if (!eventRepository.existsById(eventId)) {
            throw new IllegalArgumentException(
                    "Event not found: " + eventId
            );
        }

        return attendanceRepository.findByEventId(eventId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private EventAttendanceResponse convertToResponse(
            EventAttendance attendance) {

        EventAttendanceResponse response =
                new EventAttendanceResponse();

        response.setId(attendance.getId());

        if (attendance.getEvent() != null) {

            response.setEventId(
                    attendance.getEvent().getId()
            );

            response.setEventTitle(
                    attendance.getEvent().getTitle()
            );
        }

        if (attendance.getUser() != null) {

            response.setUserId(
                    attendance.getUser().getId()
            );

            response.setUserName(
                    attendance.getUser().getName()
            );
        }

        response.setStatus(attendance.getStatus());
        response.setMarkedAt(attendance.getMarkedAt());

        return response;
    }
}