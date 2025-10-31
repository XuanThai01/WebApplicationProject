package com.mycompany.webapp.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;



    private LocalDateTime orderDate = LocalDateTime.now();

    private BigDecimal totalAmount = BigDecimal.ZERO;

    private BigDecimal payPrice = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING) // Lưu status dưới dạng số (0,1,2,3)
    private OrderStatus status = OrderStatus.CHUA_XAC_NHAN;

    private String shippingAddress;

    private String note;

    private String imageUrl;  // URL đầy đủ, ví dụ: "/images/sp1.jpg"

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    // Thông tin khách vãng lai
    @Column(name = "guest_name", length = 100)
    private String guestName;

    @Column(name = "guest_phone", length = 20)
    private String guestPhone;

    @Column(name = "guest_email", length = 100)
    private String guestEmail;
    @Column(name="deliveryDate")
    private String deliveryDate;

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Enumerated(EnumType.STRING)
    private PayMethod payMethod;

    public PayMethod getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(PayMethod payMethod) {
        this.payMethod = payMethod;
    }
    @Enumerated(EnumType.STRING)
    private OrderStatusPay orderStatusPay = OrderStatusPay.PENDING;

    public OrderStatusPay getOrderStatusPay() {
        return orderStatusPay;
    }

    public void setOrderStatusPay(OrderStatusPay orderStatusPay) {
        this.orderStatusPay = orderStatusPay;
    }

    @Column(name = "guest_token", length = 255)
    private String guestToken;

    // Quan hệ 1-nhiều với order_details
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;

    //
    @ManyToOne
    @JoinColumn(name="customerId")
    User user;

    @ManyToOne
    @JoinColumn(name = "shipping_method_id", nullable = false)
    private ShippingMethod shippingMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UsedVoucher> usedVouchers = new HashSet<>();

    // getter & setter
    public Set<UsedVoucher> getUsedVouchers() { return usedVouchers; }
    public void setUsedVouchers(Set<UsedVoucher> usedVouchers) { this.usedVouchers = usedVouchers; }

    // Getter & Setter
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }



    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }


    public ShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }

    public String getGuestToken() {
        return guestToken;
    }

    public void setGuestToken(String guestToken) {
        this.guestToken = guestToken;
    }

    public BigDecimal getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(BigDecimal payPrice) {
        this.payPrice = payPrice;
    }
}