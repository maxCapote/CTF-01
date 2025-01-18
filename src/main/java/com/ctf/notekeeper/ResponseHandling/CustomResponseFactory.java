package com.ctf.notekeeper.ResponseHandling;

import java.util.Map;

// good ol' factory pattern for my devs out there
public class CustomResponseFactory {
    public static CustomResponse createResponse(String key, Object value) {
        CustomResponse response = new CustomResponse();
        response.setProperty(key, value);
        return response;
    }

    public static CustomResponse createResponse(Map<String, Object> properties) {
        CustomResponse response = new CustomResponse();
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            response.setProperty(entry.getKey(), entry.getValue());
        }
        return response;
    }
}
