package com.example.OrderCoffeeBE.Entity.enums;

public enum OrderStatus {
    PENDING("pending"),
    IN_PROGRESS("in-progress"), 
    COMPLETED("completed"),
    CANCELLED("cancelled");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static OrderStatus fromString(String text) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.status.equalsIgnoreCase(text)) {
                return orderStatus;
            }
        }
        throw new IllegalArgumentException("No enum constant " + OrderStatus.class.getCanonicalName() + " with text " + text);
    }
}