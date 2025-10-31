package com.mycompany.webapp.entity;

public enum PayMethod {
    COD("Thanh toán khi nhận hàng"),
    VNPAY("Thanh toán qua VNPAY");


    private final String displayName;

    PayMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
