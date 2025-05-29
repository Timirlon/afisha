package com.practice.afisha.dto.event;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {
    @NotNull
    Double lon;

    @NotNull
    Double lat;
}
