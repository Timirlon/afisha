package com.practice.afisha.dto.event;

import com.practice.afisha.dto.category.CategoryDto;
import com.practice.afisha.dto.user.UserShortDto;
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

    int views;


    public void setLocationLat(Double latValue) {
        location.setLat(latValue);
    }

    public void setLocationLon(Double lonValue) {
        location.setLon(lonValue);
    }
}
