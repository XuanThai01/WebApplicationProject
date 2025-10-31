package com.mycompany.webapp.repository;

import com.mycompany.webapp.entity.ShippingMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, Long> {

    // Ví dụ thêm: tìm theo tên
    ShippingMethod findByName(String name);
}