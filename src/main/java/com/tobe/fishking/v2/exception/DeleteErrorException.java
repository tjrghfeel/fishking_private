package com.tobe.fishking.v2.exception;

public class DeleteErrorException extends RuntimeException {

    public DeleteErrorException(String msg, Throwable t) {
        super(msg, t);
    }

    public DeleteErrorException(String msg) {
        super(msg);
    }

    public DeleteErrorException() {
        super();
    }

}
