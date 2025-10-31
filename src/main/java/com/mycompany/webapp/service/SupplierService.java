package com.mycompany.webapp.service;

import com.mycompany.webapp.entity.Supplier;
import com.mycompany.webapp.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    // Lấy toàn bộ danh sách Supplier
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    // Lấy Supplier theo ID
    public Optional<Supplier> getSupplierById(Integer id) {
        return supplierRepository.findById(id);
    }

    // Thêm hoặc cập nhật Supplier
    public Supplier saveSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    // Xóa Supplier
    public void deleteSupplier(Integer id) {
        supplierRepository.deleteById(id);
    }
}
