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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final OtpService otpService;
    private UserService userService;
    private CartService cartService;
    @Autowired
    private JavaMailSender mailSender;
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
    @PostMapping("/verifyADMIN")
    @ResponseBody
    public ResponseEntity verifyOtpADMIN(@RequestBody RegisterRequest registerRequest,
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
            user.setRole("ADMIN");
            userService.saveUser(user);

            return ResponseEntity.ok().body(Map.of("message", "Đăng ký thành công!"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Otp ko hợp lệ hoặc hết hạn"));
        }

    }
    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @RequestParam String user,
            @RequestParam String oldPass,
            @RequestParam String newPass,
            @RequestParam String confirmPass
    ) {
        // Kiểm tra mật khẩu mới và confirm
        if (!newPass.equals(confirmPass)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Mật khẩu nhập lại không trùng!"));
        }

        // Kiểm tra user tồn tại
        User existingUser = userService.findUserByUsername(user);
        if (existingUser == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Người dùng không tồn tại!"));
        }

        // Lấy mật khẩu cũ từ DB
        String dbPassword = existingUser.getPassword(); // ví dụ "{noop}123456"
        String plainDbPassword = dbPassword.startsWith("{noop}") ?
                dbPassword.substring("{noop}".length()) : dbPassword;

        // So sánh mật khẩu cũ
        if (!plainDbPassword.equals(oldPass)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Mật khẩu cũ không đúng!"));
        }

        // Cập nhật mật khẩu mới
        existingUser.setPassword("{noop}" + newPass);
        userService.save(existingUser);

        return ResponseEntity.ok(Map.of("message", "Đổi mật khẩu thành công!"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> sendOtp(@RequestBody Map<String, String> payload) {
        String user = payload.get("user");
        String email = payload.get("email");

        if (user == null || user.isEmpty() || email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "User và Email là bắt buộc"));
        }

        // Kiểm tra xem user và email có đúng
        User optUserByName = userService.findUserByUsername(user);
        if (optUserByName==null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User không tồn tại"));
        }

        User existingUser = optUserByName;

// Kiểm tra email có khớp với user không
        if (!existingUser.getUserInfo().getEmail().equalsIgnoreCase(email)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email không đúng với User"));
        }

// Nếu đúng, tiếp tục tạo mật khẩu mới và gửi email
        String newPassword = generateRandomPassword(8);
        existingUser.setPassword("{noop}" + newPassword); // hoặc bcrypt
        userService.save(existingUser);

// Gửi email
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Mật khẩu mới của bạn");
            message.setText("Xin chào " + user + ",\nMật khẩu mới của bạn là: " + newPassword);
            mailSender.send(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Gửi email thất bại"));
        }

        return ResponseEntity.ok(Map.of("message", "Mật khẩu mới đã được gửi tới email của bạn"));
    }
    // Hàm tạo mật khẩu ngẫu nhiên
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%!";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < length; i++){
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    }

