package com.mycompany.webapp.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "shipping_method")
public class ShippingMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String name;  // Economy, Express, Super Express

    @Column(length = 255)
    private String description;

    @Column(name = "estimated_days", nullable = false)
    private Integer estimatedDays;

    @Column(name = "cost", precision = 10, scale = 2, nullable = false)
    private BigDecimal cost;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ShippingStatus status = ShippingStatus.ACTIVE;

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEstimatedDays() {
        return estimatedDays;
    }

    public void setEstimatedDays(Integer estimatedDays) {
        this.estimatedDays = estimatedDays;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public ShippingStatus getStatus() {
        return status;
    }

    public void setStatus(ShippingStatus status) {
        this.status = status;
    }
    @OneToMany(mappedBy = "shippingMethod", cascade = CascadeType.ALL, orphanRemoval = false)
    private Set<Order> orders = new HashSet<>();

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}