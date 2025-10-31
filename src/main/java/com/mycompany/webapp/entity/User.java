package com.mycompany.webapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username", unique = true, length = 50)
    private String username;

    @Column(name="password",nullable = false)
    private String password;

    @Column(name="role",nullable = false, length = 20)
    private String role;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    UserInfo userInfo;

    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    private List<Cart>  cart;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Order> orders;

    public List<Cart> getCart() {
        return cart;
    }

    public void setCart(List<Cart> cart) {
        this.cart = cart;
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        if (userInfo != null) {
            userInfo.setUser(this);
        }
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
