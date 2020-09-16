package com.example.test.rest.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
class ApiError {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private LocalDateTime timestamp;
    private String field;
    private String message;
    private String debugMessage;

    ApiError(HttpStatus status) {
        this(status, null, null, null);
    }

    ApiError(HttpStatus status, Throwable ex) {
        this(status, null, "Unexpected error", ex);
    }

    ApiError(HttpStatus status, String field, String message) {
        this(status, field, message, null);
    }

    ApiError(HttpStatus status, String message, Throwable ex) {
        this(status, "entity", message, ex);
    }

    ApiError(HttpStatus status, String field, String message, Throwable ex) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.field = field;
        if (ex != null) {
            this.debugMessage = ex.getLocalizedMessage();
        }
    }
}