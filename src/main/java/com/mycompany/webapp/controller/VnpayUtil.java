package com.mycompany.webapp.controller;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VnpayUtil {

    public static String hmacSHA512(final String key, final String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hash.append('0');
                hash.append(hex);
            }
            return hash.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to generate HMACSHA512", ex);
        }
    }

    // Build query string sorted by key ascending
    public static String buildQueryString(Map<String, String> params) {
        StringBuilder query = new StringBuilder();
        try {
            List<String> keys = new ArrayList<>(params.keySet());
            Collections.sort(keys);
            boolean first = true;
            for (String k : keys) {
                String value = params.get(k);
                if (value == null || value.length() == 0) continue;
                if (!first) query.append('&');
                query.append(URLEncoder.encode(k, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(value, StandardCharsets.US_ASCII.toString()));
                first = false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return query.toString();
    }

    // Build hash data (sorted keys but NOT URL encoding keys, values: ASCII-encoded)
    public static String buildHashData(Map<String, String> params) {
        StringBuilder hashData = new StringBuilder();
        try {
            List<String> keys = new ArrayList<>(params.keySet());
            Collections.sort(keys);
            boolean first = true;
            for (String k : keys) {
                String value = params.get(k);
                if (value == null || value.length() == 0) continue;
                if (!first) hashData.append('&');
                hashData.append(k).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII.toString()));
                first = false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return hashData.toString();
    }
}
