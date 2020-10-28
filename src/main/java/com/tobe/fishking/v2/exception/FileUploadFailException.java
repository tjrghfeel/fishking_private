package com.tobe.fishking.v2.exception;

public class FileUploadFailException extends RuntimeException {

    public FileUploadFailException(String msg, Throwable t) {
        super(msg, t);
    }

    public FileUploadFailException(String msg) {
        super(msg);
    }

    public FileUploadFailException() {
        super();
    }


}
