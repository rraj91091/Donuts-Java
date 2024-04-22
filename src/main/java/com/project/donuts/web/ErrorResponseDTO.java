package com.project.donuts.web;

import java.time.LocalDateTime;

public class ErrorResponseDTO {

    private final LocalDateTime timestamp;
    private final String message;
    private final String details;

    public ErrorResponseDTO(LocalDateTime timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}