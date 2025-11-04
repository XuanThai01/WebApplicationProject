package com.mycompany.webapp.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VnpConfig {
    @Value("${vnp.payUrl}")
    public String vnpPayUrl;

    @Value("${vnp.tmnCode}")
    public String vnpTmnCode;

    @Value("${vnp.hashSecret}")
    public String vnpHashSecret;

    @Value("${vnp.returnUrl}")
    public String vnpReturnUrl;

    @Value("${vnp.ipnUrl}")
    public String vnpIpnUrl;
}
