package com.practice.afisha.exception;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiError {
    final List<String> errors = new ArrayList<>();

    String status;

    String reason;

    String message;

    String timestamp;
}
