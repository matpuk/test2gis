package com.test2gis.errorhandling;

public class ErrorMessage {
    private int status;
    private String message;

    public ErrorMessage() {}

    ErrorMessage(AppException ex){
        this.status = ex.getStatus();
        this.message = ex.getMessage();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
