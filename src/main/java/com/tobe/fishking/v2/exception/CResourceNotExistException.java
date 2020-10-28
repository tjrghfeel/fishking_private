package com.tobe.fishking.v2.exception;

public class CResourceNotExistException extends RuntimeException {
    public CResourceNotExistException(String msg, Throwable t) {
        super(msg, t);
    }

    public CResourceNotExistException(String msg) {
        super(msg);
    }

    public CResourceNotExistException() {
        super();
    }
}
