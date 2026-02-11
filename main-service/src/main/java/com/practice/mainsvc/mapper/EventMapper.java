package com.practice.mainsvc.mapper;

import com.practice.mainsvc.dto.event.*;
import com.practice.mainsvc.model.Event;

import com.practice.mainsvc.util.DateTimeFormatConstants;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor

@Component
public class EventMapper {
    CategoryMapper categoryMapper;
    UserMapper userMapper;

    public Event fromDto(NewEventDto dto) {
        int initialConfReqs = 0;

        return Event.builder()
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .locationLatitude(dto.getLocation().getLat())
                .locationLongitude(dto.getLocation().getLon())
                .title(dto.getTitle())
                .confirmedRequests(initialConfReqs)
                .date(DateTimeFormatConstants.parse(
                        dto.getEventDate()))
                .build();
    }

    public Event fromDto(UpdateEventUserRequest dto) {
        return Event.builder()
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .title(dto.getTitle())
                .date(DateTimeFormatConstants.parse(
                        dto.getEventDate()))
                .locationLatitude(dto.getLocation().getLat())
                .locationLongitude(dto.getLocation().getLon())
                .build();
    }

    public Event fromDto(UpdateEventAdminRequest dto) {
        return Event.builder()
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .title(dto.getTitle())
                .date(DateTimeFormatConstants.parse(
                        dto.getEventDate()))
                .locationLatitude(dto.getLocation().getLat())
                .locationLongitude(dto.getLocation().getLon())
                .build();
    }

    public EventFullDto toDto(Event event) {
        EventFullDto dto = EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .description(event.getDescription())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .title(event.getTitle())
                .state(event.getState().name())
                .createdOn(DateTimeFormatConstants.format(
                        event.getCreated()))
                .eventDate(DateTimeFormatConstants.format(
                        event.getDate()))
                .publishedOn(DateTimeFormatConstants.format(
                        event.getPublished()))
                .category(categoryMapper.toDto(
                        event.getCategory()))
                .initiator(userMapper.toShortDto(
                        event.getInitiator()))
                .views(event.getViews())
                .build();

        dto.setLocationLat(event.getLocationLatitude());
        dto.setLocationLon(event.getLocationLongitude());

        return dto;
    }

    public List<EventFullDto> toDto(Page<Event> events) {
        return events.stream()
                .map(this::toDto)
                .toList();
    }

    public EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .paid(event.getPaid())
                .title(event.getTitle())
                .eventDate(DateTimeFormatConstants.format(
                        event.getDate()))
                .category(categoryMapper.toDto(
                        event.getCategory()))
                .initiator(userMapper.toShortDto(
                        event.getInitiator()))
                .views(event.getViews())
                .build();
    }

    public List<EventShortDto> toShortDto(Page<Event> events) {
        return events.stream()
                .map(this::toShortDto)
                .toList();
    }
}
