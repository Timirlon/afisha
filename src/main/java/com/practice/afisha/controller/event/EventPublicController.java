package com.practice.afisha.controller.event;

import com.practice.afisha.dto.event.EventFullDto;
import com.practice.afisha.dto.event.EventShortDto;
import com.practice.afisha.exception.RequestInputException;
import com.practice.afisha.mapper.EventMapper;
import com.practice.afisha.service.EventService;
import com.practice.afisha.util.EventSort;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.practice.afisha.util.DateTimeFormatConstants.getDefaultFormatter;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RestController
@RequestMapping("/events")
public class EventPublicController {
    EventService eventService;
    EventMapper eventMapper;

    @GetMapping
    public List<EventShortDto> findAll(@RequestParam String text,
                                       @RequestParam List<Integer> categories,
                                       @RequestParam boolean paid,
                                       @RequestParam String rangeStart,
                                       @RequestParam String rangeEnd,
                                       @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                       @RequestParam String sort,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {

        LocalDateTime start;
        LocalDateTime end;
        if (!rangeStart.isBlank() && !rangeEnd.isBlank()) {
            start = getDateTimeFromString(rangeStart);
            end = getDateTimeFromString(rangeEnd);
        } else {
            start = LocalDateTime.now();
            end = null;
        }
        EventSort sortMethod = getSortFromString(sort);

        return eventMapper.toShortDto(
                eventService.findAllByMultipleParametersPublicRequest(
                        text, categories, paid, start, end, onlyAvailable, sortMethod, from, size));
    }

    @GetMapping("/{id}")
    public EventFullDto findById(@PathVariable int id) {
        return eventMapper.toDto(
                eventService.findByIdPublicRequest(id));
    }

    private EventSort getSortFromString(String strSort) {
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
