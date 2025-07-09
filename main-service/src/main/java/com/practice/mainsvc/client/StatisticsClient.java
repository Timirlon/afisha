package com.practice.mainsvc.client;

import com.practice.mainsvc.model.Event;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.practice.mainsvc.util.DateTimeFormatConstants.format;
import static com.practice.mainsvc.util.RequestConstants.getClientIp;
import static java.time.LocalDateTime.now;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@Component
public class StatisticsClient {
    RestTemplate restTemplate;

    public StatisticsClient(@Value("${stats-service.server.url}") String svcUri,
                            RestTemplateBuilder builder) {

        restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(svcUri))
                .build();
    }

    public ResponseEntity<Object> hit(String endpointUri,
                                      HttpServletRequest servletRequest) {
        EndpointHit hitEntity = new EndpointHit();
        String formattedDate = format(now());

        String ip = getClientIp(servletRequest);
        hitEntity.setApp("afisha-app");
        hitEntity.setUri(endpointUri);
        hitEntity.setIp(ip);
        hitEntity.setTimestamp(formattedDate);


        return restTemplate.postForEntity("/hit", hitEntity, Object.class);
    }

    public ResponseEntity<List<ViewStats>> view(String start,
                                                String end,
                                                String[] uris,
                                                boolean unique) {

        return restTemplate.exchange(
                "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ViewStats>>() {},
                Map.of("start", start,
                        "end", end,
                        "uris", uris,
                        "unique", unique));
    }

    public void setViewsToEvent(Event event) {
        String[] uris = {String.format("/events/%d", event.getId())};

        List<ViewStats> response = view(
                format(LocalDateTime.now().minusYears(1)),
                format(LocalDateTime.now()),
                uris,
                true).getBody();

        if (response == null || response.isEmpty()) {
            return;
        }

        event.setViews(response.getFirst().getHits());
    }

    public void setViewsToEvent(Page<Event> events) {
        String[] uris = events.stream()
                .map(event -> String.format("/events/%d", event.getId()))
                .toArray(String[]::new);

        List<ViewStats> response = view(
                format(LocalDateTime.now().minusYears(1)),
                format(LocalDateTime.now()),
                uris,
                true).getBody();

        if (response == null) {
            return;
        }

        events.stream()
                .forEach(event -> response.forEach(stats -> {
                    if (stats.getUri().equals(
                            String.format("/events/%d", event.getId()))) {
                        event.setViews(stats.getHits());
                    }
                }));
    }
}
