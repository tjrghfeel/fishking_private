package com.tobe.fishking.v2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
public class TideException extends Exception {

    private static final long serialVersionUID = 1L;

    public TideException(String message){
        super(message);
    }
}
