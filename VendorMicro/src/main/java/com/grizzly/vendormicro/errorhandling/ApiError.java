package com.grizzly.vendormicro.errorhandling;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ApiError {
    private HttpStatus status; // HTTP response status
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp; // timestamp of when the error occurred (server time)
    private String message; // a message for displaying to user
    private String debugMessage; // a message for logging/debugging
    private List<ApiSubError> subErrors; // sub-errors when multiple errors occurred in one call (eg. multiple validation errors)

    ApiError() {
        timestamp = LocalDateTime.now();
    }

    ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    ApiError(HttpStatus status, Throwable e) {
        this(status);
        this.message = "Unexpected error.";
        this.debugMessage = e.getLocalizedMessage();
    }

    ApiError(HttpStatus status, Throwable e, String message) {
        this(status, e);
        this.message = message;
    }

    public ApiError(HttpStatus status, String message, String debugMessage) {
        this(status);
        this.message = message;
        this.debugMessage = debugMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public List<ApiSubError> getSubErrors() {
        return subErrors;
    }

    public void setSubErrors(List<ApiSubError> subErrors) {
        this.subErrors = subErrors;
    }
}
