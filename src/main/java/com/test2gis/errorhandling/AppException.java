package com.test2gis.errorhandling;

public class AppException extends Exception {
    private Integer status;

    public AppException(int status, String message) {
        super(message);
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
