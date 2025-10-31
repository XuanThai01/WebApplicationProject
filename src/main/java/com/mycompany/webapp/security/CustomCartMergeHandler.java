package com.mycompany.webapp.security;

import com.mycompany.webapp.entity.Cart;
import com.mycompany.webapp.entity.User;
import com.mycompany.webapp.service.CartService;
import com.mycompany.webapp.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomCartMergeHandler {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Transactional
    public void mergeGuestCartToUser(HttpServletRequest req, Authentication auth) {
        String username = auth.getName();
        User user = userService.findUserByUsername(username);
        System.out.println("đã chạy vào merge");

        // 1. Lấy guestToken từ request attribute trước
        String guestToken = (String) req.getAttribute("guestToken");

        // 2. Nếu null, thử lấy từ cookie client
        if (guestToken == null) {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if ("GUEST_TOKEN".equals(c.getName())) {
                        guestToken = c.getValue();
                        break;
                    }
                }
            }
        }

        System.out.println(guestToken);
        if (guestToken != null) {
            List<Cart> guestCartList = cartService.getCartByGuestToken(guestToken);
            List<Cart> userCartList = cartService.getCartByUserId(user.getId());

            for (Cart guestItem : guestCartList) {
                boolean found = false;
                for (Cart userItem : userCartList) {
                    if (userItem.getProductVariant().getId().equals(guestItem.getProductVariant().getId())) {
                        userItem.setQuantity(userItem.getQuantity() + guestItem.getQuantity());
                        cartService.save(userItem);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    guestItem.setUser(user);
                    guestItem.setGuestToken(null);
                    cartService.save(guestItem);
                }
            }

            cartService.deleteCartByGuestToken(guestToken);

        }
    }
}
