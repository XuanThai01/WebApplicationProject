package com.mycompany.webapp.repository;

import com.mycompany.webapp.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Cart c join fetch c.productVariant pv join fetch pv.productDetail pd where c.guestToken = :guestToken")
     List<Cart> findBytoken(@Param("guestToken")String guestToken);

    List<Cart> findByGuestToken(String guestToken);
    List<Cart> findByUserId(Long userId);

    void deleteAllByGuestToken(String guestToken);
}