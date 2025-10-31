package com.mycompany.webapp.repository;

import com.mycompany.webapp.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    // Có thể thêm query tùy chỉnh nếu cần, ví dụ:
    // List<Supplier> findByCountry(String country);
}
