package com.practice.mainsvc.exception;

import static com.practice.mainsvc.util.DateTimeFormatConstants.getDefault;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(RequestInputException e) {
        log.warn(e.getMessage());

        String reason = "Incorrectly made request.";
        return buildApiError(HttpStatus.BAD_REQUEST, reason, e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handle(NotFoundException e) {
        log.warn(e.getMessage());

        String reason = "The required object was not found.";
        return buildApiError(HttpStatus.NOT_FOUND, reason, e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(SQLException e) {
        log.warn(e.getMessage());

        String reason = "Integrity constraint has been violated.";
        return buildApiError(HttpStatus.CONFLICT, reason, e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(InvalidConditionException e) {
        log.warn(e.getMessage());

        String reason = "For the requested operation the conditions are not met.";
        return buildApiError(HttpStatus.CONFLICT, reason, e.getMessage(), LocalDateTime.now());
    }

    private ApiError buildApiError(HttpStatus status, String reason,
                                   String message, LocalDateTime timestamp) {

        ApiError apiError = new ApiError();

        apiError.setStatus(status.name());
        apiError.setReason(reason);
        apiError.setMessage(message);
        apiError.setTimestamp(timestamp.format(getDefault()));

        return apiError;
    }
}
