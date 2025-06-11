package com.practice.afisha.service;

import com.practice.afisha.exception.NotFoundException;
import com.practice.afisha.model.Event;
import com.practice.afisha.repository.EventRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@Service
public class StatisticsService {
    EventRepository eventRepository;

    public void addView(Event event) {
        event.setViews(
                event.getViews() + 1);

        eventRepository.save(event);
    }

    public void addViewById(int eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event with id: %d not found", eventId)));

        event.setViews(
                event.getViews() + 1);

        eventRepository.save(event);
    }

    public void addView(Page<Event> events) {
        List<Event> updatedEvents = events.stream()
                .peek(event -> event.setViews(event.getViews() + 1))
                .toList();

        eventRepository.saveAll(updatedEvents);
    }
}
