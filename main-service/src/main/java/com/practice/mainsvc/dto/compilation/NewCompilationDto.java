package com.practice.mainsvc.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCompilationDto {

    final Set<Integer> events = new HashSet<>();

    Boolean pinned;

    @NotBlank
    @Size(min = 1, max = 50)
    String title;
}
