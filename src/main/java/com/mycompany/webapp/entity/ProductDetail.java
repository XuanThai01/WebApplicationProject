package com.mycompany.webapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productdetail")
@Data // bao gồm Getter, Setter, ToString, EqualsAndHashCode
 @NoArgsConstructor @AllArgsConstructor
@Builder
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pd_id;
    @Column(name = "Name")
    private String name;
    private Integer status;

    @Column(name = "descriptiondetail")
    private String descriptiondetail;

    @Column(name = "imgProduct")
    private  String imgProduct;

    public String getImgProduct() {
        return imgProduct;
    }

    public void setImgProduct(String imgProduct) {
        this.imgProduct = imgProduct;
    }

    public Long getPd_id() {
        return pd_id;
    }

    public void setPd_id(Long pd_id) {
        this.pd_id = pd_id;
    }

    @ManyToOne
    @JoinColumn(name = "P_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "S_id")
    private Supplier supplier;

    @OneToMany(mappedBy = "productDetail",fetch = FetchType.EAGER)
    List<ProductVariant> productVariants =new ArrayList<>();

    public List<ProductVariant> getProductVariants() {
        return productVariants;
    }

    public void setProductVariants(List<ProductVariant> productVariants) {
        this.productVariants = productVariants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescriptiondetail() {
        return descriptiondetail;
    }

    public void setDescriptiondetail(String descriptiondetail) {
        this.descriptiondetail = descriptiondetail;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }


}
