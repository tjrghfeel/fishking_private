package com.tobe.fishking.v2.exception;




public class FileImageOnlyException extends RuntimeException {

    public FileImageOnlyException(String msg, Throwable t) {
        super(msg, t);
    }

    public FileImageOnlyException(String msg) {
        super(msg);
    }

    public FileImageOnlyException() {
        super();
    }

}
