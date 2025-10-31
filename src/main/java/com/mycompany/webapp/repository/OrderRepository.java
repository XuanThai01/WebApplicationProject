package com.mycompany.webapp.repository;

import com.mycompany.webapp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Lấy tất cả đơn hàng của user
    List<Order> findByUserId(Long userId);

    // Lấy tất cả đơn hàng theo guestToken
    List<Order> findByGuestToken(String guestToken);

    // Có thể thêm các query khác theo trạng thái, ngày tạo, vv.
    List<Order> findByStatus(String status);
}
