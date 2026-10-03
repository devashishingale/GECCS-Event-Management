package com.geccs.eventmanagement.service;

import com.geccs.eventmanagement.dto.EventCreateRequest;
import com.geccs.eventmanagement.dto.EventResponse;
import com.geccs.eventmanagement.entity.Council;
import com.geccs.eventmanagement.entity.Event;
import com.geccs.eventmanagement.entity.User;
import com.geccs.eventmanagement.repository.CouncilRepository;
import com.geccs.eventmanagement.repository.EventRepository;
import com.geccs.eventmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CouncilRepository councilRepository;

    public EventService(
            EventRepository eventRepository,
            UserRepository userRepository,
            CouncilRepository councilRepository) {

        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.councilRepository = councilRepository;
    }

    public List<EventResponse> getAllEvents() {

        return eventRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public EventResponse getEventById(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Event not found: " + id
                        )
                );

        return convertToResponse(event);
    }

    @Transactional
    public EventResponse createEvent(EventCreateRequest request) {

        if (request.getTitle() == null ||
                request.getTitle().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Event title is required"
            );
        }

        if (request.getVenue() == null ||
                request.getVenue().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Event venue is required"
            );
        }

        if (request.getStartTime() == null) {
            throw new IllegalArgumentException(
                    "Event start time is required"
            );
        }

        if (request.getEndTime() == null) {
            throw new IllegalArgumentException(
                    "Event end time is required"
            );
        }

        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new IllegalArgumentException(
                    "End time must be after start time"
            );
        }

        if (request.getRegistrationDeadline() != null &&
                request.getRegistrationDeadline()
                        .isAfter(request.getStartTime())) {

            throw new IllegalArgumentException(
                    "Registration deadline must be before event start time"
            );
        }

        if (request.getMaxParticipants() != null &&
                request.getMaxParticipants() <= 0) {

            throw new IllegalArgumentException(
                    "Maximum participants must be greater than zero"
            );
        }

        if (request.getCreatedByUserId() == null) {
            throw new IllegalArgumentException(
                    "Creator user ID is required"
            );
        }

        if (request.getCouncilCode() == null ||
                request.getCouncilCode().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Council code is required"
            );
        }

        User user = userRepository
                .findById(request.getCreatedByUserId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found: "
                                        + request.getCreatedByUserId()
                        )
                );

        if (!user.isEmailVerified()) {
            throw new IllegalArgumentException(
                    "User email is not verified"
            );
        }

        if (!"ACTIVE".equalsIgnoreCase(
                user.getAccountStatus())) {

            throw new IllegalArgumentException(
                    "User account is not active"
            );
        }

        Council council = councilRepository
                .findByCode(request.getCouncilCode())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Council not found: "
                                        + request.getCouncilCode()
                        )
                );

        if (!council.isActive()) {
            throw new IllegalArgumentException(
                    "Council is inactive"
            );
        }

        if (council.getAcademicUnit() != null) {

            if (user.getAcademicUnit() == null) {
                throw new IllegalArgumentException(
                        "User is not linked to an academic unit"
                );
            }

            if (!council.getAcademicUnit()
                    .getId()
                    .equals(user.getAcademicUnit().getId())) {

                throw new IllegalArgumentException(
                        "User academic unit does not match the council"
                );
            }
        }

        Event event = new Event();

        event.setTitle(request.getTitle().trim());
        event.setDescription(request.getDescription());
        event.setPosterUrl(request.getPosterUrl());
        event.setVenue(request.getVenue().trim());
        event.setStartTime(request.getStartTime());
        event.setEndTime(request.getEndTime());
        event.setRegistrationDeadline(
                request.getRegistrationDeadline()
        );
        event.setMaxParticipants(
                request.getMaxParticipants()
        );

        event.setStatus("PUBLISHED");
        event.setCreatedBy(user);
        event.setCouncil(council);

        Event savedEvent = eventRepository.save(event);

        return convertToResponse(savedEvent);
    }

    private EventResponse convertToResponse(Event event) {

        EventResponse response = new EventResponse();

        response.setId(event.getId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setPosterUrl(event.getPosterUrl());
        response.setVenue(event.getVenue());
        response.setStartTime(event.getStartTime());
        response.setEndTime(event.getEndTime());
        response.setRegistrationDeadline(
                event.getRegistrationDeadline()
        );
        response.setMaxParticipants(
                event.getMaxParticipants()
        );
        response.setStatus(event.getStatus());

        if (event.getCreatedBy() != null) {

            response.setCreatedByUserId(
                    event.getCreatedBy().getId()
            );

            response.setCreatedByName(
                    event.getCreatedBy().getName()
            );
        }

        if (event.getCouncil() != null) {

            response.setCouncilName(
                    event.getCouncil().getName()
            );

            response.setCouncilCode(
                    event.getCouncil().getCode()
            );
        }

        return response;
    }
}