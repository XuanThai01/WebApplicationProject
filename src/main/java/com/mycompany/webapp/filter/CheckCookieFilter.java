package com.mycompany.webapp.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.UUID;


public class CheckCookieFilter implements Filter {

    public static String COOKIE_NAME = "GUEST_TOKEN";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest rq = (HttpServletRequest) servletRequest;
        HttpServletResponse rp =(HttpServletResponse) servletResponse;
        Cookie[] cookies = rq.getCookies();
        String guestToken = null;

        if (cookies != null) {
            for (Cookie c : cookies) {
                if (COOKIE_NAME.equals(c.getName())) {
                    guestToken = c.getValue();
                }
            }
        }

        if (guestToken == null) {
            guestToken = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(COOKIE_NAME, guestToken);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(60 * 60 * 24 * 30); // 30 ngày
            rp.addCookie(cookie);
        }
        rq.setAttribute("guestToken", guestToken);

        // Cho phép request đi tiếp

        filterChain.doFilter(rq, rp);
    }




}
