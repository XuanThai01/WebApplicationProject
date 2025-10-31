package com.mycompany.webapp.service;

import com.mycompany.webapp.entity.Order;
import com.mycompany.webapp.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Lưu hoặc cập nhật đơn hàng
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    // Lấy tất cả đơn hàng
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    // Lấy đơn hàng theo ID
    public Optional<Order> getById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    // Lấy đơn hàng theo user
    public List<Order> getByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // Lấy đơn hàng theo guest token
    public List<Order> getByGuestToken(String guestToken) {
        return orderRepository.findByGuestToken(guestToken);
    }

    // Xoá đơn hàng
    public void deleteById(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}