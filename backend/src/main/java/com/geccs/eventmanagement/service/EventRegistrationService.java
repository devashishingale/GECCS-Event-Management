package com.geccs.eventmanagement.service;

import com.geccs.eventmanagement.dto.EventRegistrationRequest;
import com.geccs.eventmanagement.dto.EventRegistrationResponse;
import com.geccs.eventmanagement.entity.Event;
import com.geccs.eventmanagement.entity.EventRegistration;
import com.geccs.eventmanagement.entity.User;
import com.geccs.eventmanagement.repository.EventRegistrationRepository;
import com.geccs.eventmanagement.repository.EventRepository;
import com.geccs.eventmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EventRegistrationService {

    private final EventRegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventRegistrationService(
            EventRegistrationRepository registrationRepository,
            EventRepository eventRepository,
            UserRepository userRepository) {

        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public EventRegistrationResponse registerForEvent(
            EventRegistrationRequest request) {

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

        if (!"PUBLISHED".equalsIgnoreCase(
                event.getStatus())) {

            throw new IllegalArgumentException(
                    "Registration is available only for published events"
            );
        }

        LocalDateTime now = LocalDateTime.now();

        if (event.getStartTime() != null &&
                !event.getStartTime().isAfter(now)) {

            throw new IllegalArgumentException(
                    "Registration is closed because the event has started"
            );
        }

        if (event.getRegistrationDeadline() != null &&
                now.isAfter(event.getRegistrationDeadline())) {

            throw new IllegalArgumentException(
                    "Registration deadline has passed"
            );
        }

        if (registrationRepository.existsByEventIdAndUserId(
                event.getId(),
                user.getId())) {

            throw new IllegalArgumentException(
                    "User is already registered for this event"
            );
        }

        if (event.getMaxParticipants() != null) {

            long registrationCount =
                    registrationRepository.countByEventId(
                            event.getId()
                    );

            if (registrationCount >=
                    event.getMaxParticipants()) {

                throw new IllegalArgumentException(
                        "Event registration limit has been reached"
                );
            }
        }

        EventRegistration registration =
                new EventRegistration();

        registration.setEvent(event);
        registration.setUser(user);
        registration.setStatus("REGISTERED");

        EventRegistration savedRegistration =
                registrationRepository.save(registration);

        return convertToResponse(savedRegistration);
    }

    private EventRegistrationResponse convertToResponse(
            EventRegistration registration) {

        EventRegistrationResponse response =
                new EventRegistrationResponse();

        response.setId(registration.getId());

        if (registration.getEvent() != null) {

            response.setEventId(
                    registration.getEvent().getId()
            );

            response.setEventTitle(
                    registration.getEvent().getTitle()
            );
        }

        if (registration.getUser() != null) {

            response.setUserId(
                    registration.getUser().getId()
            );

            response.setUserName(
                    registration.getUser().getName()
            );
        }

        response.setStatus(registration.getStatus());
        response.setRegisteredAt(
                registration.getRegisteredAt()
        );

        return response;
    }
}