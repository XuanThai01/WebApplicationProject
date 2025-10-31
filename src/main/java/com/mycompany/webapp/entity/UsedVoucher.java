package com.mycompany.webapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "used_voucher")
public class UsedVoucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Voucher được dùng
    @ManyToOne
    @JoinColumn(name = "voucher_id", nullable = false)
    private Voucher voucher;

    // Order mà voucher được áp dụng
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Ai dùng voucher: userId hoặc guestToken
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "guest_token", length = 100)
    private String guestToken;

    @Column(name = "used_at")
    private LocalDateTime usedAt;


    // getter & setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Voucher getVoucher() { return voucher; }
    public void setVoucher(Voucher voucher) { this.voucher = voucher; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getGuestToken() { return guestToken; }
    public void setGuestToken(String guestToken) { this.guestToken = guestToken; }

    public LocalDateTime getUsedAt() { return usedAt; }
    public void setUsedAt(LocalDateTime usedAt) { this.usedAt = usedAt; }
}