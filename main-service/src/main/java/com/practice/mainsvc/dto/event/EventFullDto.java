package com.practice.mainsvc.dto.event;

import com.practice.mainsvc.dto.category.CategoryDto;
import com.practice.mainsvc.dto.user.UserShortDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {
    String annotation;

    CategoryDto category;

    int confirmedRequests;

    String createdOn;

    String description;

    String eventDate;

    int id;

    UserShortDto initiator;

    final Location location = new Location();

    Boolean paid;

    int participantLimit;

    String publishedOn;

    Boolean requestModeration;

    String state;

    String title;

    long views;


    public void setLocationLat(Double latValue) {
        location.setLat(latValue);
    }

    public void setLocationLon(Double lonValue) {
        location.setLon(lonValue);
    }
}
