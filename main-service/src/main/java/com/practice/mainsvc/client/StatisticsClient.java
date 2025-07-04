package com.practice.mainsvc.client;

import com.practice.statssvc.dto.EndpointHit;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

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
}
