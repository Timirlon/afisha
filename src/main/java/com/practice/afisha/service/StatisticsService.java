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
import java.util.Set;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@Service
public class StatisticsService {
    EventRepository eventRepository;

    Set<String> uniqueViews;

    public void addView(Event event, String ipAddress) {
        String encoded = encode(ipAddress, event.getId());

        if (!uniqueViews.contains(encoded)) {
            event.setViews(
                    event.getViews() + 1);

            eventRepository.save(event);
            uniqueViews.add(encoded);
        }
    }

    public void addViewById(int eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event with id: %d not found", eventId)));

        event.setViews(
                event.getViews() + 1);

        eventRepository.save(event);
    }

    public void addView(Page<Event> events, String ipAddress) {
        List<Event> updatedEvents = events.stream()
                .filter(event -> {
                    String encoded = encode(ipAddress, event.getId());

                    if (!uniqueViews.contains(encoded)) {
                        event.setViews(
                                event.getViews() + 1);

                        uniqueViews.add(encoded);
                        return true;
                    }

                    return false;
                })
                .toList();

        eventRepository.saveAll(updatedEvents);
    }

    private String encode(String ip, int eventId) {
        return String.format("ip:%s,event:%d", ip, eventId);
    }
}
