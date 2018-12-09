package com.softgarden.baselibrary.network;

/**
 * Created by MirkoWu on 2017/5/25 0025.
 */

public class ApiException extends RuntimeException {

    private int status;
    private String message;

    public ApiException(String message) {
        super(message);
        this.message = message;
    }

    public ApiException(int status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
