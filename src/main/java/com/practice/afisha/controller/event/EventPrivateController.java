package com.practice.afisha.controller.event;

import com.practice.afisha.dto.event.*;
import com.practice.afisha.exception.RequestInputException;
import com.practice.afisha.mapper.EventMapper;
import com.practice.afisha.model.Event;
import com.practice.afisha.service.EventService;
import com.practice.afisha.util.UserStateAction;
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

    @GetMapping
    public List<EventShortDto> findAllByInitiatorId(@PathVariable int userId,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        return eventMapper.toShortDto(
                eventService.findAllByInitiatorId(userId, from, size));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@RequestBody @Valid NewEventDto eventRequest,
                               @PathVariable int userId) {
        Event event = eventMapper.fromDto(eventRequest);
        int categoryId = eventRequest.getCategory();

        return eventMapper.toDto(
                eventService.create(event, userId, categoryId));
    }

    @GetMapping("/{eventId}")
    public EventFullDto findById(@PathVariable int eventId,
                                 @PathVariable int userId) {

        return eventMapper.toDto(
                eventService.findByIdPublicRequest(eventId, userId));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateById(@PathVariable int eventId,
                                   @RequestBody @Valid UpdateEventUserRequest updateRequest,
                                   @PathVariable int userId) {
        Event event = eventMapper.fromDto(updateRequest);

        Integer categoryId = updateRequest.getCategory();
        UserStateAction stateAction = getStateAction(updateRequest.getStateAction());


        return eventMapper.toDto(
                eventService.updateByIdUserRequest(eventId, event, userId, categoryId, stateAction));
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
}
