package com.mycompany.webapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Ct_id")
    private Integer id;

    @Column(columnDefinition = "TEXT") // ánh xạ kiểu TEXT
    private String description;

    @OneToMany(mappedBy ="category")
    List<Product> products = new ArrayList<>();

}