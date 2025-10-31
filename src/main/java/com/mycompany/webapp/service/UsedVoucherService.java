package com.mycompany.webapp.service;

import com.mycompany.webapp.entity.Order;
import com.mycompany.webapp.entity.UsedVoucher;
import com.mycompany.webapp.entity.Voucher;
import com.mycompany.webapp.repository.UsedVoucherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsedVoucherService {

    private final UsedVoucherRepository usedVoucherRepository;

    public UsedVoucherService(UsedVoucherRepository usedVoucherRepository) {
        this.usedVoucherRepository = usedVoucherRepository;
    }

    // Lưu hoặc cập nhật UsedVoucher
    public UsedVoucher save(UsedVoucher usedVoucher) {
        return usedVoucherRepository.save(usedVoucher);
    }

    // Lấy tất cả voucher đã dùng theo order
    public List<UsedVoucher> getByOrderId(Long orderId) {
        return usedVoucherRepository.findByOrderOrderId(orderId);
    }



    // Lấy tất cả voucher đã dùng theo guestToken
    public List<UsedVoucher> getByGuestToken(String guestToken) {
        return usedVoucherRepository.findByGuestToken(guestToken);
    }

    // Kiểm tra voucher đã dùng cho order chưa
    public boolean exists(Long orderId, Long voucherId) {
        return usedVoucherRepository.existsByOrderOrderIdAndVoucherId(orderId, voucherId);
    }

    // ----------------------
    // Method mới: lưu voucher đã dùng cho order
    // ----------------------
    public List<UsedVoucher> applyVouchers(Order order, List<Voucher> vouchers) {
        // Nếu danh sách null hoặc rỗng → bỏ qua
        if (vouchers == null || vouchers.isEmpty()) {
            return List.of(); // trả về list rỗng
        }

        List<UsedVoucher> usedVouchers = vouchers.stream()
                .map(voucher -> {
                    UsedVoucher uv = new UsedVoucher();
                    uv.setOrder(order);
                    uv.setVoucher(voucher);
                    uv.setUsedAt(java.time.LocalDateTime.now());
                    return save(uv); // gọi method save() đã có
                })
                .toList();

        return usedVouchers;
    }

    public List<UsedVoucher> getAll() {
      return usedVoucherRepository.findAll();
    }
}
