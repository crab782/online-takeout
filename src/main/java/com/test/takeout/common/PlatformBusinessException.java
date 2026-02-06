package com.test.takeout.common;

public class PlatformBusinessException extends RuntimeException {
    private int code;

    public PlatformBusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
