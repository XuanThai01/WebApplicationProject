package com.mycompany.webapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final JavaMailSender mailSender;
    private final int otpLength;
    private final long expireSeconds;

    // lưu in-memory: key = email (hoặc phone), value = OtpEntry
    private Map<String, OtpEntry> otpStorage = new ConcurrentHashMap<>();
    private Random random = new Random();

    public OtpService(JavaMailSender mailSender,
                      @Value("${app.otp.length:6}") int otpLength,
                      @Value("${app.otp.expire-minutes:5}") int expireMinutes) {
        this.mailSender = mailSender;
        this.otpLength = otpLength;
        this.expireSeconds = expireMinutes * 60L;
    }

    public String generateOtpFor(String email) {
        String otp = generateNumericOtp(otpLength);
        Instant expiresAt = Instant.now().plusSeconds(expireSeconds);
        otpStorage.put(email, new OtpEntry(otp, expiresAt));
        sendOtpEmail(email, otp);
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        OtpEntry entry = otpStorage.get(email);
        if (entry == null) return false;
        if (Instant.now().isAfter(entry.expiresAt)) {
            otpStorage.remove(email);
            return false;
        }
        boolean ok = entry.otp.equals(otp);
        if (ok) {
            otpStorage.remove(email); // dùng một lần
        }
        return ok;
    }

    private String generateNumericOtp(int length) {
        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length) - 1;
        int value = min + random.nextInt(max - min + 1);
        return String.valueOf(value);
    }

    private void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Mã OTP xác thực đăng ký");
            message.setText("Mã OTP của bạn là: " + otp + "\nMã có hiệu lực trong " + (expireSeconds/60) + " phút.");
            message.setFrom("no-reply@example.com");
            mailSender.send(message);
        } catch (Exception e) {
            // xử lý lỗi gửi mail ở đây (log, retry, v.v.)
            e.printStackTrace();
        }
    }

    private static class OtpEntry {
        final String otp;
        final Instant expiresAt;
        OtpEntry(String otp, Instant expiresAt) {
            this.otp = otp;
            this.expiresAt = expiresAt;
        }
    }
}