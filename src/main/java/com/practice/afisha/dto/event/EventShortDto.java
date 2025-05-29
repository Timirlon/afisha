package com.practice.afisha.dto.event;

import com.practice.afisha.dto.category.CategoryDto;
import com.practice.afisha.dto.user.UserShortDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDto {
    String annotation;

    CategoryDto category;

    int confirmedRequests;

    String eventDate;

    int id;

    UserShortDto initiator;

    Boolean paid;

    String title;

    int views;
}
