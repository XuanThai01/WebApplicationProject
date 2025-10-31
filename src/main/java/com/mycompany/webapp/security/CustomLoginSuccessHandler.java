package com.mycompany.webapp.security;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import com.mycompany.webapp.security.MyUserDetails;
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    public CustomLoginSuccessHandler() {

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Lấy userId từ Principal (tùy thuộc UserDetails bạn định nghĩa)
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        // Lấy guest_token từ cookie
        String guestToken = null;
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("guest_token".equals(c.getName())) {
                    guestToken = c.getValue();
                    break;
                }
            }
        }
        try {
            response.sendRedirect("/products");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("vào file success thành công");
    }

    public void mergeGuestCartToUser(HttpServletRequest req, Authentication auth) {

    }

    /*
         // Nếu có guestToken thì merge ngay
        if (guestToken != null) {
            cartService.mergeCart(guestToken, userId);

            // Xóa cookie guest_token sau khi merge
            Cookie cookie = new Cookie("guest_token", "");
            cookie.setMaxAge(0); // hết hạn ngay
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        // Redirect về trang chính hoặc giỏ hàng
        try {
            response.sendRedirect("/cart/view");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
         */
}
