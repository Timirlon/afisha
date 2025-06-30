package com.practice.mainsvc.dto.compilation;

import com.practice.mainsvc.dto.event.EventShortDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    List<EventShortDto> events;

    int id;

    boolean pinned;

    String title;
}
