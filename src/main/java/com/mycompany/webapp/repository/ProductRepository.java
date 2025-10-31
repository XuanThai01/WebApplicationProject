package com.mycompany.webapp.repository;

import com.mycompany.webapp.entity.Product;
import com.mycompany.webapp.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product,Integer> {

}
