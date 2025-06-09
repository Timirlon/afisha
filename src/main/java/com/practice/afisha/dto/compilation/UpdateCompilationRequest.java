package com.practice.afisha.dto.compilation;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompilationRequest {
    final Set<Integer> events = new HashSet<>();

    Boolean pinned;

    @Size(min = 1, max = 50)
    String title;
}
