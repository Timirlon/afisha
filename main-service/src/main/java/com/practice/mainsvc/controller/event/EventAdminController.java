package com.practice.mainsvc.controller.event;

import com.practice.mainsvc.client.StatisticsClient;
import com.practice.mainsvc.util.AdminStateAction;
import com.practice.mainsvc.dto.event.EventFullDto;
import com.practice.mainsvc.dto.event.UpdateEventAdminRequest;
import com.practice.mainsvc.exception.RequestInputException;
import com.practice.mainsvc.mapper.EventMapper;
import com.practice.mainsvc.model.Event;
import com.practice.mainsvc.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
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

    StatisticsClient statisticsClient;

    @GetMapping
    public List<EventFullDto> findAllByMultipleParameters(@RequestParam(required = false) Collection<Integer> users,
                                                          @RequestParam(required = false) Collection<String> states,
                                                          @RequestParam(required = false) Collection<Integer> categories,
                                                          @RequestParam(required = false) String rangeStart,
                                                          @RequestParam(required = false) String rangeEnd,
                                                          @RequestParam(defaultValue = "0") int from,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          HttpServletRequest servletRequest) {

        List<EventFullDto> result = eventMapper.toDto(
                eventService.findAllByMultipleParametersAdminRequest(
                        users, states, categories, rangeStart, rangeEnd, from, size));


        statisticsClient.hit("/admin/events", servletRequest);


        return result;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateById(@PathVariable int eventId,
                                   @RequestBody @Valid UpdateEventAdminRequest eventRequest,
                                   HttpServletRequest servletRequest) {

        Event event = eventMapper.fromDto(eventRequest);
        int categoryId;
        if (eventRequest.getCategory() != null) {
            categoryId = eventRequest.getCategory();
        } else {
            categoryId = 0;
        }
        AdminStateAction stateAction = getStateAction(eventRequest.getStateAction());

        EventFullDto result = eventMapper.toDto(
                eventService.updateByIdAdminRequest(eventId, event, categoryId, stateAction));

        statisticsClient.hit("/admin/events/" + eventId, servletRequest);


        return result;
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
