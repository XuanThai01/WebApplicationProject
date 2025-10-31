package com.mycompany.webapp.repository;

import com.mycompany.webapp.entity.UsedVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsedVoucherRepository extends JpaRepository<UsedVoucher, Long> {

    // Lấy tất cả voucher đã dùng của một order
    List<UsedVoucher> findByOrderOrderId(Long orderId);


    // Lấy tất cả voucher đã dùng với guest token
    List<UsedVoucher> findByGuestToken(String guestToken);

    // Kiểm tra voucher đã dùng cho order chưa
    boolean existsByOrderOrderIdAndVoucherId(Long orderId, Long voucherId);
}