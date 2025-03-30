package com.chatApplication.Authentication.exceptions;

public class ApiResponse {
    private boolean success;
    private String message;
    private Object data; // Optional field for additional response data

    // Constructor for responses without data
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Constructor for responses with data
    public ApiResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}

