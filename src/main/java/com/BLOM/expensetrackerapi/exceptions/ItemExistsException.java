package com.BLOM.expensetrackerapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ItemExistsException extends RuntimeException{
    private static final long serialVersion = 1L;


    public ItemExistsException(String message) {
        super(message);
    }
}
