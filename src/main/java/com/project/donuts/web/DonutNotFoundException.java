package com.project.donuts.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_GATEWAY)
public class DonutNotFoundException extends RuntimeException {

    public DonutNotFoundException(String message) {
        super(message);
    }
}
