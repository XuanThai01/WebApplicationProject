package com.mycompany.webapp.controller;

import com.mycompany.webapp.entity.DiscountType;
import com.mycompany.webapp.entity.Voucher;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
@Component("VoucherUtil")
public class VoucherUtil {

    // Tính số tiền giảm thực tế (áp dụng maxDiscount)
    public static BigDecimal getDiscountForProduct(Voucher v, BigDecimal productPrice) {
        BigDecimal discount= null;
        if (v.getDiscountType() == DiscountType.AMOUNT) {
            discount = v.getDiscountValue();
        } else { // PERCENT
            discount = productPrice.multiply(v.getDiscountValue()).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
            if (v.getMaxDiscount().compareTo(BigDecimal.ZERO) > 0 && discount.compareTo(v.getMaxDiscount())>0) {
                return null; // không áp dụng vì vượt maxDiscount
            }
        }
        return discount;
    }

    // Kiểm tra voucher có thể áp dụng với giá sản phẩm không
    public static boolean isValidForProduct(Voucher v, BigDecimal productPrice) {
        return productPrice.compareTo(v.getMinOrderValue())>0;
    }

    // Comparator tổng hợp: voucher hợp lệ đứng đầu, giảm nhiều trước, không hợp lệ xuống cuối
    public static Comparator<Voucher> getVoucherComparator(BigDecimal productPrice) {
        return (v1, v2) -> {
            boolean valid1 = isValidForProduct(v1, productPrice);
            boolean valid2 = isValidForProduct(v2, productPrice);

            BigDecimal discount1 = getDiscountForProduct(v1, productPrice);
            BigDecimal discount2 = getDiscountForProduct(v2, productPrice);

            boolean usable1 = valid1 && discount1 != null;
            boolean usable2 = valid2 && discount2 != null;

            // Ưu tiên voucher usable (hợp lệ và có discount)
            if (usable1 && !usable2) return -1;
            if (!usable1 && usable2) return 1;

            // Nếu cả hai cùng usable → so sánh giảm dần theo discount
            if (usable1 && usable2) {
                return discount2.compareTo(discount1); // giảm dần
            }

            // Nếu cả hai cùng không usable → để nguyên thứ tự
            return 0;
        };
    }

}
/*
public static Comparator<Voucher> getVoucherComparator(BigDecimal price) {
    return Comparator.<Voucher>comparing(
                    v -> isValidForProduct(v, price) && getDiscountForProduct(v, price) != null
            ).reversed() // hợp lệ lên đầu
            .thenComparing(
                    v -> {
                        BigDecimal d = getDiscountForProduct(v, price);
                        return d != null ? d : BigDecimal.ZERO;
                    },
                    Comparator.reverseOrder() // giảm dần theo discount
            );
}

 */