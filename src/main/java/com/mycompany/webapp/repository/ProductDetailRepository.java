package com.mycompany.webapp.repository;

import com.mycompany.webapp.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail,Long> {

    // Lấy cả Customer và danh sách Orders cùng lúc
    @Query("SELECT DISTINCT pd FROM ProductDetail pd JOIN FETCH pd.productVariants ")
    List<ProductDetail> findAllWithPdvariant();

    // lấy từng cái ProductDetail
    @Query("Select pd from ProductDetail pd JOIN FETCH pd.productVariants where pd.pd_id = :pd_id")
    ProductDetail findProductDetailWithProductVariant(@Param("pd_id") Long pd_id);

    @Query("SELECT p FROM ProductDetail p WHERE lower(p.name) LIKE lower(concat('%', :keyword, '%'))")
    List<ProductDetail> searchByName(@Param("keyword") String keyword);

    @Query("SELECT pd FROM ProductDetail pd WHERE pd.product.id = :productId")
    List<ProductDetail> findByProductId(@Param("productId") Integer productId);
}
