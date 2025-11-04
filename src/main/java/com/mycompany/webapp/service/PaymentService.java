package com.mycompany.webapp.service;


import com.mycompany.webapp.configuration.VnpConfig;
import com.mycompany.webapp.controller.VnpayUtil;
import com.mycompany.webapp.entity.Order;
import com.mycompany.webapp.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
        order.setDescription("đơn hàng đang được thanh toán bằng cổng thanh toán VNPAY");
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

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        vnpParams.put("vnp_CreateDate", formatter.format(cld.getTime()));
        cld.add(Calendar.MINUTE, 15);
        vnpParams.put("vnp_ExpireDate", formatter.format(cld.getTime()));

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
