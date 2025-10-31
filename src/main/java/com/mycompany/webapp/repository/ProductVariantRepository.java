package com.mycompany.webapp.repository;

import com.mycompany.webapp.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant,Long> {

    @Query("select pv  from ProductVariant pv JOIN FETCH pv.productDetail where pv.id = :variantId")
     ProductVariant findPvWithPdById(@Param("variantId")Long variantId);
}
