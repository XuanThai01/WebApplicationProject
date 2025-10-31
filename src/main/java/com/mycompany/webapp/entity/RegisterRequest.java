package com.mycompany.webapp.entity;

import jakarta.persistence.Entity;
import org.springframework.stereotype.Component;

@Component
public class RegisterRequest {
    private String email;
    private String fullname;
    private String password;
    private String otp; // thêm field OTP

    // getters & setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
}

