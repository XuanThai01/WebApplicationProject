package com.mycompany.webapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guest_token")
    private String guestToken;

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "pd_variant_id")
    ProductVariant productVariant;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;


    // ===== Constructors =====
    public Cart() {
    }


    // ===== Getters & Setters =====
    public Long getId() {
        return id;
    }

    public String getGuestToken() {
        return guestToken;
    }

    public void setGuestToken(String guestToken) {
        this.guestToken = guestToken;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

