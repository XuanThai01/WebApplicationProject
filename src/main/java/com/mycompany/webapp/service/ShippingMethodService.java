package com.mycompany.webapp.service;

import com.mycompany.webapp.entity.ShippingMethod;
import com.mycompany.webapp.repository.ShippingMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ShippingMethodService {

    @Autowired
    private ShippingMethodRepository shippingMethodRepository;

    // ✅ Lấy tất cả phương thức vận chuyển
    public List<ShippingMethod> findAll() {
        return shippingMethodRepository.findAll();
    }

    // ✅ Tìm theo ID
    public Optional<ShippingMethod> findById(Long id) {
        return shippingMethodRepository.findById(id);
    }

    // ✅ Lưu hoặc cập nhật
    public ShippingMethod save(ShippingMethod shippingMethod) {
        return shippingMethodRepository.save(shippingMethod);
    }

    // ✅ Xóa theo ID
    public void deleteById(Long id) {
        shippingMethodRepository.deleteById(id);
    }

    // ✅ Tìm theo tên
    public ShippingMethod findByName(String name) {
        return shippingMethodRepository.findByName(name);
    }
}