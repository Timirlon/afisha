package com.practice.statssvc.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EndpointHit {
    String app;
    String uri;
    String ip;
    String timestamp;
}
