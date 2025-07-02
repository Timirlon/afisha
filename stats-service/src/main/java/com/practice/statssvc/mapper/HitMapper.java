package com.practice.statssvc.mapper;

import com.practice.statssvc.dto.EndpointHit;
import com.practice.statssvc.model.Hit;
import org.springframework.stereotype.Component;

import static com.practice.statssvc.util.DateTimeFormatConstants.parse;

@Component
public class HitMapper {
    public Hit fromDto(EndpointHit endpointHit) {
        Hit entity = new Hit();

        entity.setApp(endpointHit.getApp());
        entity.setIp(endpointHit.getIp());
        entity.setUri(endpointHit.getUri());
        entity.setCreated(
                parse(endpointHit.getTimestamp()));

        return entity;
    }
}
