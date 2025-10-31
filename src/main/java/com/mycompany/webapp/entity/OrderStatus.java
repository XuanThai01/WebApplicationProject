package com.mycompany.webapp.entity;

public enum OrderStatus {
    CHUA_XAC_NHAN(0, "Chưa xác nhận"),
    DANG_XU_LY(1, "Đang xử lý"),
    DA_XAC_NHAN(2, "Đã xác nhận"),
    THANH_CONG(3,"đã giao thành công"),
    HUY(4, "Hủy đơn hàng");

    private final int code;
    private final String displayName;

    OrderStatus(int code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public int getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static OrderStatus fromCode(int code) {
        for (OrderStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown OrderStatus code: " + code);
    }
}