package com.mycompany.webapp.service;


import com.mycompany.webapp.entity.Product;
import com.mycompany.webapp.entity.ProductDetail;
import com.mycompany.webapp.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getproductbyid(Integer productId) {
        return productRepository.findById(productId).get();
    }
}

