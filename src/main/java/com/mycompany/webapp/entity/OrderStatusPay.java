package com.mycompany.webapp.entity;

public enum OrderStatusPay {
    PENDING,   // Chưa thanh toán
    PAID,      // Đã thanh toán
    SHIPPED,   // Đã giao
    CANCELLED; // Hủy

    @Override
    public String toString() {
        return name(); // trả về "PAID", "PENDING", ...
    }
}
