package com.practice.mainsvc.service;

import com.practice.mainsvc.model.Event;
import com.practice.mainsvc.repository.EventRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.practice.mainsvc.util.RequestConstants.getClientIp;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@Service
public class StatisticsService {
    EventRepository eventRepository;

    Set<String> uniqueViews = new HashSet<>();


    public void addView(Event event, HttpServletRequest servletRequest) {
        String ipAddress = getClientIp(servletRequest);

        String encoded = encode(ipAddress, event.getId());

        if (!uniqueViews.contains(encoded)) {
            event.setViews(
                    event.getViews() + 1);

            eventRepository.save(event);
            uniqueViews.add(encoded);
        }
    }

    public void addView(Page<Event> events, HttpServletRequest servletRequest) {
        String ipAddress = getClientIp(servletRequest);

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
