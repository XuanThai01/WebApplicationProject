package com.mycompany.webapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productvariant")
@Data // bao gồm Getter, Setter, ToString, EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant {

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(length = 50, nullable = false)
    private String color;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Column(length = 20, nullable = false)
    private String size;

    @Column(nullable = false)
    private Integer quantity;

    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
    }

    @Column()
    private String material;

    @Column
    private Long price;

    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    @OneToMany(mappedBy = "productVariant")
    List<Cart> carts = new ArrayList<>();

    @OneToMany(mappedBy = "productVariant")
    List<OrderDetail> orderDetails;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
