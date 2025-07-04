package com.practice.mainsvc.mapper;

import com.practice.mainsvc.dto.event.*;
import com.practice.mainsvc.model.Event;
import static com.practice.mainsvc.util.DateTimeFormatConstants.getDefault;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor

@Component
public class EventMapper {
    CategoryMapper categoryMapper;
    UserMapper userMapper;

    public Event fromDto(NewEventDto eventDto) {
        Event event = new Event();

        event.setAnnotation(eventDto.getAnnotation());
        event.setDescription(eventDto.getDescription());
        event.setLocationLatitude(eventDto.getLocation().getLat());
        event.setLocationLongitude(eventDto.getLocation().getLon());
        event.setTitle(eventDto.getTitle());
        event.setViews(0);
        event.setConfirmedRequests(0);

        event.setDate(
                LocalDateTime.parse(eventDto.getEventDate(), getDefault()));

        // Значения по умолчанию
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        } else {
            event.setPaid(false);
        }

        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        } else {
            event.setParticipantLimit(0);
        }

        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        } else {
            event.setRequestModeration(true);
        }

        return event;
    }

    public Event fromDto(UpdateEventUserRequest eventDto) {
        Event event = new Event();

        event.setAnnotation(eventDto.getAnnotation());
        event.setDescription(eventDto.getDescription());
        event.setPaid(eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        event.setRequestModeration(eventDto.getRequestModeration());
        event.setTitle(eventDto.getTitle());

        if (eventDto.getLocation() != null) {
            event.setLocationLatitude(eventDto.getLocation().getLat());
            event.setLocationLongitude(eventDto.getLocation().getLon());
        }

        if (eventDto.getEventDate() != null) {
            event.setDate(
                    LocalDateTime.parse(eventDto.getEventDate(), getDefault()));
        }


        return event;
    }

    public Event fromDto(UpdateEventAdminRequest eventDto) {
        Event event = new Event();

        event.setAnnotation(eventDto.getAnnotation());
        event.setDescription(eventDto.getDescription());
        event.setPaid(eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        event.setRequestModeration(eventDto.getRequestModeration());
        event.setTitle(eventDto.getTitle());

        if (eventDto.getLocation() != null) {
            event.setLocationLatitude(eventDto.getLocation().getLat());
            event.setLocationLongitude(eventDto.getLocation().getLon());
        }

        if (eventDto.getEventDate() != null) {
            event.setDate(
                    LocalDateTime.parse(eventDto.getEventDate(), getDefault()));
        }


        return event;
    }

    public EventFullDto toDto(Event event) {
        EventFullDto eventDto = new EventFullDto();

        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setConfirmedRequests(event.getConfirmedRequests());
        eventDto.setDescription(event.getDescription());
        eventDto.setId(event.getId());
        eventDto.setPaid(event.getPaid());
        eventDto.setParticipantLimit(event.getParticipantLimit());
        eventDto.setRequestModeration(event.getRequestModeration());
        eventDto.setTitle(event.getTitle());
        eventDto.setViews(event.getViews());

        eventDto.setState(event.getState().name());

        eventDto.setLocationLat(event.getLocationLatitude());
        eventDto.setLocationLon(event.getLocationLongitude());

        eventDto.setCreatedOn(
                event.getCreated().format(getDefault()));

        eventDto.setEventDate(
                event.getDate().format(getDefault()));

        if (event.getPublished() != null) {
            eventDto.setPublishedOn(
                    event.getPublished().format(getDefault()));
        }

        eventDto.setCategory(
                categoryMapper.toDto(event.getCategory()));

        eventDto.setInitiator(
                userMapper.toShortDto(event.getInitiator()));


        return eventDto;
    }

    public List<EventFullDto> toDto(Page<Event> events) {
        return events.stream()
                .map(this::toDto)
                .toList();
    }

    public EventShortDto toShortDto(Event event) {
        EventShortDto shortDto = new EventShortDto();

        shortDto.setAnnotation(event.getAnnotation());
        shortDto.setConfirmedRequests(event.getConfirmedRequests());
        shortDto.setId(event.getId());
        shortDto.setPaid(event.getPaid());
        shortDto.setTitle(event.getTitle());
        shortDto.setViews(event.getViews());

        shortDto.setEventDate(
                event.getDate().format(getDefault()));

        shortDto.setCategory(
                categoryMapper.toDto(event.getCategory()));

        shortDto.setInitiator(
                userMapper.toShortDto(event.getInitiator()));

        return shortDto;
    }

    public List<EventShortDto> toShortDto(Page<Event> events) {
        return events.stream()
                .map(this::toShortDto)
                .toList();
    }
}
