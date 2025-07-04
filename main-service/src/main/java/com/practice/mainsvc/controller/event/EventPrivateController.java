package com.practice.mainsvc.controller.event;

import com.practice.mainsvc.client.StatisticsClient;
import com.practice.mainsvc.dto.event.*;
import com.practice.mainsvc.dto.request.EventRequestStatusUpdateRequest;
import com.practice.mainsvc.dto.request.EventRequestStatusUpdateResult;
import com.practice.mainsvc.dto.request.ParticipationRequestDto;
import com.practice.mainsvc.exception.RequestInputException;
import com.practice.mainsvc.mapper.EventMapper;
import com.practice.mainsvc.mapper.RequestMapper;
import com.practice.mainsvc.model.ConfirmationStatus;
import com.practice.mainsvc.model.Event;
import com.practice.mainsvc.service.EventService;
import com.practice.mainsvc.service.RequestService;
import com.practice.mainsvc.util.UserStateAction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RestController
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {
    EventService eventService;
    EventMapper eventMapper;

    RequestService requestService;
    RequestMapper requestMapper;

    StatisticsClient statisticsClient;

    @GetMapping
    public List<EventShortDto> findAllByInitiatorId(@PathVariable int userId,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    HttpServletRequest servletRequest) {
        List<EventShortDto> result = eventMapper.toShortDto(
                eventService.findAllByInitiatorId(userId, from, size));

        statisticsClient.hit(String.format("/users/%d/events", userId), servletRequest);

        return result;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@RequestBody @Valid NewEventDto eventRequest,
                               @PathVariable int userId,
                               HttpServletRequest servletRequest) {
        Event event = eventMapper.fromDto(eventRequest);
        int categoryId = eventRequest.getCategory();

        EventFullDto result = eventMapper.toDto(
                eventService.create(event, userId, categoryId));

        statisticsClient.hit(String.format("/users/%d/events", userId), servletRequest);


        return result;
    }

    @GetMapping("/{eventId}")
    public EventFullDto findById(@PathVariable int eventId,
                                 @PathVariable int userId,
                                 HttpServletRequest servletRequest) {

        EventFullDto result = eventMapper.toDto(
                eventService.findByIdPublicRequest(eventId, userId));

        statisticsClient.hit(String.format("/users/%d/events/%d", userId, eventId), servletRequest);


        return result;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateById(@PathVariable int eventId,
                                   @RequestBody @Valid UpdateEventUserRequest updateRequest,
                                   @PathVariable int userId,
                                   HttpServletRequest servletRequest) {
        Event event = eventMapper.fromDto(updateRequest);

        Integer categoryId = updateRequest.getCategory();
        UserStateAction stateAction = getStateAction(updateRequest.getStateAction());


        EventFullDto result = eventMapper.toDto(
                eventService.updateByIdUserRequest(eventId, event, userId, categoryId, stateAction));

        statisticsClient.hit(String.format("/users/%d/events/%d", userId, eventId), servletRequest);


        return result;
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> findAllByEventIdAndInitiatorId(@PathVariable int eventId,
                                                                        @PathVariable int userId,
                                                                        HttpServletRequest servletRequest) {
        List<ParticipationRequestDto> result = requestMapper.toDto(
                requestService.findAllByEventIdAndInitiatorId(eventId, userId));

        statisticsClient.hit(String.format("/users/%d/events/%d/requests", userId, eventId), servletRequest);


        return result;
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult changeEventRequestStatus(@RequestBody EventRequestStatusUpdateRequest dto,
                                                                   @PathVariable int eventId,
                                                                   @PathVariable int userId,
                                                                   HttpServletRequest servletRequest) {

        List<Integer> requestIds = dto.getRequestIds();
        ConfirmationStatus status = getStatus(dto.getStatus());


        EventRequestStatusUpdateResult result =  requestMapper.toUpdDto(
                requestService.changeEventRequestStatus(requestIds, status, eventId, userId));

        eventService.updateConfirmedRequests(eventId, result.getConfirmedRequests().size());

        statisticsClient.hit(String.format("/users/%d/events/%d/requests", userId, eventId), servletRequest);

        return result;
    }

    private UserStateAction getStateAction(String strStateAction) {
        if (strStateAction == null || strStateAction.isBlank()) {
            return null;
        }

        if (strStateAction.equals(
                UserStateAction.SEND_TO_REVIEW.name())) {
            return UserStateAction.SEND_TO_REVIEW;
        }

        if (strStateAction.equals(
                UserStateAction.CANCEL_REVIEW.name())) {
            return UserStateAction.CANCEL_REVIEW;
        }


        throw new RequestInputException("Invalid state action data.");
    }

    private ConfirmationStatus getStatus(String strStatus) {
        if (strStatus.equals(
                ConfirmationStatus.CONFIRMED.name())) {
            return ConfirmationStatus.CONFIRMED;
        }

        if (strStatus.equals(
                ConfirmationStatus.REJECTED.name())) {
            return ConfirmationStatus.REJECTED;
        }

        throw new RequestInputException("Invalid confirmation status for request.");
    }
}
