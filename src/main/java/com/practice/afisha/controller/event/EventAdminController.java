package com.practice.afisha.controller.event;

import com.practice.afisha.util.AdminStateAction;
import com.practice.afisha.dto.event.EventFullDto;
import com.practice.afisha.dto.event.UpdateEventAdminRequest;
import com.practice.afisha.exception.RequestInputException;
import com.practice.afisha.mapper.EventMapper;
import com.practice.afisha.model.Event;
import com.practice.afisha.model.PublicationState;
import com.practice.afisha.service.EventService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor

@RestController
@RequestMapping("/admin/events")
public class EventAdminController {
    EventService eventService;
    EventMapper eventMapper;

    @GetMapping
    public List<EventFullDto> findAllByMultipleParameters(@RequestParam(required = false) Collection<Integer> users,
                                                          @RequestParam(required = false) Collection<String> states,
                                                          @RequestParam(required = false) Collection<Integer> categories,
                                                          @RequestParam(required = false) String rangeStart,
                                                          @RequestParam(required = false) String rangeEnd,
                                                          @RequestParam(defaultValue = "0") int from,
                                                          @RequestParam(defaultValue = "10") int size) {

        return eventMapper.toDto(
                eventService.findAllByMultipleParametersAdminRequest(
                        users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateById(@PathVariable int eventId,
                                   @RequestBody @Valid UpdateEventAdminRequest eventRequest) {

        Event event = eventMapper.fromDto(eventRequest);
        int categoryId;
        if (eventRequest.getCategory() != null) {
            categoryId = eventRequest.getCategory();
        } else {
            categoryId = 0;
        }
        AdminStateAction stateAction = getStateAction(eventRequest.getStateAction());

        return eventMapper.toDto(
                eventService.updateByIdAdminRequest(eventId, event, categoryId, stateAction));
    }

    private AdminStateAction getStateAction(String strAction) {
        if (strAction == null || strAction.isBlank()) {
            return null;
        }

        if (strAction.equals(
                AdminStateAction.PUBLISH_EVENT.name())) {
            return AdminStateAction.PUBLISH_EVENT;
        }

        if (strAction.equals(
                AdminStateAction.REJECT_EVENT.name())) {
            return AdminStateAction.REJECT_EVENT;
        }

        throw new RequestInputException("Invalid state action data.");
    }
}
