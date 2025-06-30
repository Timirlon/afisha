package com.practice.mainsvc.dto.event;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000)
    String annotation;

    Integer category;

    @Size(min = 20, max = 7000)
    String description;

    String eventDate;

    Location location;

    Boolean paid;

    Integer participantLimit;

    Boolean requestModeration;

    String stateAction;

    @Size(min = 3, max = 120)
    String title;
}
