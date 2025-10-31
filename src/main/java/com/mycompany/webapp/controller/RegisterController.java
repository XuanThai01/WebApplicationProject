package com.mycompany.webapp.controller;




import com.mycompany.webapp.entity.RegisterRequest;
import com.mycompany.webapp.entity.User;
import com.mycompany.webapp.entity.UserInfo;
import com.mycompany.webapp.entity.VerifyOtpRequest;
import com.mycompany.webapp.service.CartService;
import com.mycompany.webapp.service.OtpService;
import com.mycompany.webapp.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final OtpService otpService;
    private UserService userService;
    private CartService cartService;

    public RegisterController(OtpService otpService,UserService userService,CartService cartService) {
        this.otpService = otpService;
        this.userService = userService;
        this.cartService=cartService;
    }


    @GetMapping
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    // Bước 1: gửi OTP
    @PostMapping("/send-otp")
    @ResponseBody
    public ResponseEntity sendOtp(@ModelAttribute RegisterRequest registerRequest, Model model) {
        if (registerRequest.getEmail() == null || registerRequest.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email bắt buộc"));
        } else {
            otpService.generateOtpFor(registerRequest.getEmail());
            return ResponseEntity
                    .ok()
                    .body(Map.of("message", "OTP đã được gửi tới email của bạn",
                            "registerRequest",registerRequest));
        }

    }

    // Bước 2: xác thực OTP & hoàn tất đăng ký
    @PostMapping("/verify")
    @ResponseBody
    public ResponseEntity verifyOtp(@RequestBody RegisterRequest registerRequest,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {

        boolean ok = otpService.verifyOtp(registerRequest.getEmail(), registerRequest.getOtp());
        if (ok) {
            // TODO: tạo user trong DB
            User user =new User();
            UserInfo userInfo = new UserInfo();

            userInfo.setEmail(registerRequest.getEmail());


            user.setUserInfo(userInfo);
            user.setUsername(registerRequest.getFullname());
            user.setPassword("{noop}"+registerRequest.getPassword());
            user.setRole("USER");
            userService.saveUser(user);

            String guestToken = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("GUEST_TOKEN".equals(cookie.getName())) {
                        guestToken = cookie.getValue();
                        break;
                    }
                }
            }

            // 3. Merge giỏ hàng guest -> user
            if (guestToken != null) {
                cartService.mergeCart(user, guestToken);

                // Xóa cookie sau khi merge
               /* Cookie cookie = new Cookie("cart", "");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                */
                HttpSession session = request.getSession();
                session.setAttribute("username", user.getUsername()); // 🔥 lưu vào session
            }
            return ResponseEntity.ok().body(Map.of("message", "Đăng ký thành công!"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Otp ko hợp lệ hoặc hết hạn"));
        }

    }
}
