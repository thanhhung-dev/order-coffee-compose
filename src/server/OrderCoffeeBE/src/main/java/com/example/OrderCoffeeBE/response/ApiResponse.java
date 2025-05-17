package com.example.OrderCoffeeBE.response;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class ApiResponse<T> {
    public String message;
    public boolean success;
    public T data;
    public ApiResponse(String message, boolean success, T data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, true, data);
    }
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message, false, null);
    }
}
