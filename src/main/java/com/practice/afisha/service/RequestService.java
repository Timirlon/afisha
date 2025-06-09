package com.practice.afisha.service;

import com.practice.afisha.exception.InvalidConditionException;
import com.practice.afisha.exception.NotFoundException;
import com.practice.afisha.model.*;
import com.practice.afisha.repository.EventRepository;
import com.practice.afisha.repository.RequestRepository;
import com.practice.afisha.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@Service
public class RequestService {
    RequestRepository requestRepository;
    UserRepository userRepository;
    EventRepository eventRepository;

    public List<ParticipationRequest> findAllByRequesterId(int requesterId) {
        User requester = findUserById(requesterId);

        return requestRepository.findAllByRequester_Id(requesterId);
    }

    public ParticipationRequest createNew(int requesterId, int eventId) {
        ParticipationRequest participationRequest = new ParticipationRequest();

        User requester = findUserById(requesterId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event with id=%d was not found", eventId)));


        if (requesterId == event.getInitiator().getId()) {
            throw new InvalidConditionException("Application to your own events is forbidden.");
        }

        if (event.getState() != PublicationState.PUBLISHED) {
            throw new InvalidConditionException("Event not published yet.");
        }

        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new InvalidConditionException("Participant limit has been reached.");
        }

        if (!event.getRequestModeration()) {
            participationRequest.setStatus(ConfirmationStatus.CONFIRMED);
        } else {
            participationRequest.setStatus(ConfirmationStatus.PENDING);
        }

        participationRequest.setRequester(requester);
        participationRequest.setEvent(event);
        participationRequest.setCreated(LocalDateTime.now());

        requestRepository.save(participationRequest);


        return participationRequest;
    }

    public ParticipationRequest cancel(int requestId, int userId) {
        User user = findUserById(userId);

        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Request with id=%d was not found", requestId)));

        if (userId != request.getRequester().getId()) {
            throw new NotFoundException(
                    String.format("Request with id=%d was not found", requestId));
        }

        request.setStatus(ConfirmationStatus.CANCELED);

        requestRepository.save(request);


        return request;
    }

    private User findUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("User with id=%d was not found", userId)));
    }
}
