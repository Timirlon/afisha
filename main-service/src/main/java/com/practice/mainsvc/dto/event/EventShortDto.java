package com.practice.mainsvc.dto.event;

import com.practice.mainsvc.dto.category.CategoryDto;
import com.practice.mainsvc.dto.user.UserShortDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class EventShortDto {
    String annotation;

    CategoryDto category;

    int confirmedRequests;

    String eventDate;

    int id;

    UserShortDto initiator;

    Boolean paid;

    String title;

    long views;
}
