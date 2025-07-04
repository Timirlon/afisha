package com.practice.mainsvc.controller.event;

import com.practice.mainsvc.client.StatisticsClient;
import com.practice.mainsvc.dto.event.EventFullDto;
import com.practice.mainsvc.dto.event.EventShortDto;
import com.practice.mainsvc.exception.RequestInputException;
import com.practice.mainsvc.mapper.EventMapper;
import com.practice.mainsvc.model.Event;
import com.practice.mainsvc.service.EventService;
import com.practice.mainsvc.service.StatisticsService;
import com.practice.mainsvc.util.EventSort;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.practice.mainsvc.util.DateTimeFormatConstants.getDefault;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RestController
@RequestMapping("/events")
public class EventPublicController {
    EventService eventService;
    EventMapper eventMapper;

    StatisticsService statisticsService;

    StatisticsClient statisticsClient;

    @GetMapping
    public List<EventShortDto> findAll(@RequestParam(required = false) String text,
                                       @RequestParam(required = false) List<Integer> categories,
                                       @RequestParam(required = false) Boolean paid,
                                       @RequestParam(required = false) String rangeStart,
                                       @RequestParam(required = false) String rangeEnd,
                                       @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                       @RequestParam(required = false) String sort,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size,
                                       HttpServletRequest servletRequest) {

        EventSort sortMethod = getSortFromString(sort);

        Page<Event> result = eventService.findAllByMultipleParametersPublicRequest(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sortMethod, from, size);


        statisticsService.addView(result, servletRequest);

        statisticsClient.hit("/events", servletRequest);

        return eventMapper.toShortDto(result);
    }

    @GetMapping("/{id}")
    public EventFullDto findById(@PathVariable int id, HttpServletRequest servletRequest) {
        Event result = eventService.findByIdPublicRequest(id);

        statisticsService.addView(result, servletRequest);

        statisticsClient.hit(String.format("/events/%d", id), servletRequest);

        return eventMapper.toDto(result);
    }

    private EventSort getSortFromString(String strSort) {
        if (strSort == null || strSort.isBlank()) {
            return null;
        }

        if (strSort.equals(
                EventSort.EVENT_DATE.name())) {
            return EventSort.EVENT_DATE;
        }

        if (strSort.equals(
                EventSort.VIEWS.name())) {
            return EventSort.VIEWS;
        }

        throw new RequestInputException("Invalid sorting method provided.");
    }
}
