package com.test2gis.errorhandling;

import javax.ws.rs.core.Response;

public class NotFoundException extends AppException {

    public NotFoundException(String message) {
        super(Response.Status.NOT_FOUND.getStatusCode(), message);
    }
}
