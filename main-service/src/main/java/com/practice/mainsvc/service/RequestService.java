package com.practice.mainsvc.service;

import com.practice.mainsvc.exception.InvalidConditionException;
import com.practice.mainsvc.exception.NotFoundException;
import com.practice.mainsvc.model.*;
import com.practice.mainsvc.repository.EventRepository;
import com.practice.mainsvc.repository.RequestRepository;
import com.practice.mainsvc.repository.UserRepository;
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
        User requester = findUserByIdOrElseThrow(requesterId);

        return requestRepository.findAllByRequester_Id(requesterId);
    }

    public ParticipationRequest createNew(int requesterId, int eventId) {
        ParticipationRequest participationRequest = new ParticipationRequest();

        User requester = findUserByIdOrElseThrow(requesterId);

        Event event = findEventByIdOrElseThrow(eventId);


        if (requesterId == event.getInitiator().getId()) {
            throw new InvalidConditionException("Application to your own events is forbidden.");
        }

        if (event.getState() != PublicationState.PUBLISHED) {
            throw new InvalidConditionException("Event not published yet.");
        }

        if (event.getConfirmedRequests() >= event.getParticipantLimit()
        && event.getParticipantLimit() > 0) {
            throw new InvalidConditionException("Participant limit has been reached.");

        }

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            participationRequest.setStatus(ConfirmationStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);

        } else {
            participationRequest.setStatus(ConfirmationStatus.PENDING);
        }

        participationRequest.setRequester(requester);
        participationRequest.setEvent(event);
        participationRequest.setCreated(LocalDateTime.now());

        requestRepository.save(participationRequest);
        eventRepository.save(event);

        return participationRequest;
    }

    public ParticipationRequest cancel(int requestId, int userId) {
        User user = findUserByIdOrElseThrow(userId);

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

    public List<ParticipationRequest> changeEventRequestStatus(List<Integer> requestIds, ConfirmationStatus status,
                                                               int eventId, int userId) {
        findUserByIdOrElseThrow(userId);

        Event event = findEventByIdOrElseThrow(eventId);

        if (event.getInitiator().getId() != userId) {
            throw new NotFoundException(
                    String.format("Event with id=%d was not found", eventId));
        }

        if (status == ConfirmationStatus.CONFIRMED
                && event.getParticipantLimit() == event.getConfirmedRequests()) {

            throw new InvalidConditionException("The participant limit has been reached.");
        }

        List<ParticipationRequest> requests = requestRepository.findAllByIdInAndEventId(requestIds, eventId);

        int count = 0;
        for (ParticipationRequest request : requests) {
            if (request.getStatus() != ConfirmationStatus.PENDING) {
                throw new InvalidConditionException(
                        String.format("Requests with %s status can not be changed.", request.getStatus()));
            }

            if (status == ConfirmationStatus.CONFIRMED
            && event.getConfirmedRequests() + count < event.getParticipantLimit()) {
                request.setStatus(status);
                count++;
                continue;
            }

            request.setStatus(ConfirmationStatus.REJECTED);
        }

        requestRepository.saveAll(requests);


        return requests;
    }

    public List<ParticipationRequest> findAllByEventIdAndInitiatorId(int eventId, int initiatorId) {
        return requestRepository.findAllByEvent_IdAndEvent_Initiator_Id(eventId, initiatorId);
    }

    private User findUserByIdOrElseThrow(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("User with id=%d was not found", userId)));
    }

    private Event findEventByIdOrElseThrow(int eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event with id=%d was not found", eventId)));
    }
}
