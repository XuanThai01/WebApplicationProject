package com.mycompany.webapp.service;


import com.mycompany.webapp.configuration.VnpConfig;
import com.mycompany.webapp.controller.VnpayUtil;
import com.mycompany.webapp.entity.Order;
import com.mycompany.webapp.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PaymentService {
    private final OrderRepository orderRepository;
    private final VnpConfig vnpConfig;

    public PaymentService(OrderRepository orderRepository, VnpConfig vnpConfig){
        this.orderRepository = orderRepository;
        this.vnpConfig = vnpConfig;
    }

    public Order createOrder(Order order){
        String txnRef = getRandomNumber(8);
        order.setTxnRef(txnRef);
        order.setDescription("The order is being processed through the VNPAY payment gateway.");
        return orderRepository.save(order);
    }

    public Optional<Order> findByTxnRef(String txnRef){
        return orderRepository.findByTxnRef(txnRef);
    }

    public String buildPaymentUrl(Order order, String clientIp, String locale, String bankCode) {
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", vnpConfig.vnpTmnCode);
        // amount must be multiplied by 100
        vnpParams.put("vnp_Amount", order.getPayPrice()
                .multiply(BigDecimal.valueOf(100))
                .toPlainString());
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", order.getTxnRef());
        vnpParams.put("vnp_OrderInfo", order.getDescription());
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_ReturnUrl", vnpConfig.vnpReturnUrl);
        vnpParams.put("vnp_IpAddr", clientIp);

        if (locale != null && !locale.isEmpty()) vnpParams.put("vnp_Locale", locale); else vnpParams.put("vnp_Locale","vn");

        if (bankCode != null && !bankCode.isEmpty()) vnpParams.put("vnp_BankCode", bankCode);

        ZoneId zone = ZoneId.of("Asia/Ho_Chi_Minh");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now(zone);

        // Tạo thời gian tạo giao dịch
        vnpParams.put("vnp_CreateDate", now.format(formatter));

        // Thời gian hết hạn: +15 phút
        LocalDateTime expire = now.plusMinutes(15);
        vnpParams.put("vnp_ExpireDate", expire.format(formatter));

        // build hash data
        String hashData = VnpayUtil.buildHashData(vnpParams);
        String secureHash = VnpayUtil.hmacSHA512(vnpConfig.vnpHashSecret, hashData);

        String query = VnpayUtil.buildQueryString(vnpParams);
        query += "&vnp_SecureHash=" + secureHash;

        return vnpConfig.vnpPayUrl + "?" + query;
    }

    private String getRandomNumber(int len) {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) sb.append(rnd.nextInt(10));
        return sb.toString();
    }
}
