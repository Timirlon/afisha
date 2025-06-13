package com.practice.afisha.controller.event;

import com.practice.afisha.dto.event.EventFullDto;
import com.practice.afisha.dto.event.EventShortDto;
import com.practice.afisha.exception.RequestInputException;
import com.practice.afisha.mapper.EventMapper;
import com.practice.afisha.model.Event;
import com.practice.afisha.service.EventService;
import com.practice.afisha.service.StatisticsService;
import com.practice.afisha.util.EventSort;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.practice.afisha.util.DateTimeFormatConstants.getDefaultFormatter;
import static com.practice.afisha.util.RequestConstants.getClientIp;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RestController
@RequestMapping("/events")
public class EventPublicController {
    EventService eventService;
    EventMapper eventMapper;

    StatisticsService statisticsService;


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
                                       HttpServletRequest request) {

        EventSort sortMethod = getSortFromString(sort);

        Page<Event> result = eventService.findAllByMultipleParametersPublicRequest(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sortMethod, from, size);

        String ipAddress = getClientIp(request);
        statisticsService.addView(result, ipAddress);

        return eventMapper.toShortDto(result);
    }

    @GetMapping("/{id}")
    public EventFullDto findById(@PathVariable int id, HttpServletRequest request) {
        Event result = eventService.findByIdPublicRequest(id);

        String ipAddress = getClientIp(request);
        statisticsService.addView(result, ipAddress);

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

    private LocalDateTime getDateTimeFromString(String strDate) {
        return LocalDateTime.parse(strDate, getDefaultFormatter());
    }
}
