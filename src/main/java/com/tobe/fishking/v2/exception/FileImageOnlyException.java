package com.tobe.fishking.v2.exception;


import com.sun.tools.javac.main.Option;

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
