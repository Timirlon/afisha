package com.practice.afisha.dto.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation;

    @NotNull
    Integer category;

    @NotBlank
    @Size(min = 20, max = 7000)
    String description;

    @NotBlank
    String eventDate;

    @NotNull
    @Valid
    Location location;

    Boolean paid;

    Integer participantLimit;

    Boolean requestModeration;

    @NotBlank
    @Size(min = 3, max = 120)
    String title;
}
