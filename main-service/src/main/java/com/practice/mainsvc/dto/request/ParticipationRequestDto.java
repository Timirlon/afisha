package com.practice.mainsvc.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ParticipationRequestDto {
    String created;

    Integer event;

    Integer id;

    Integer requester;

    String status;
}
