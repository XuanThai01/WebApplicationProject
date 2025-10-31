package com.mycompany.webapp.controller;


import com.mycompany.webapp.service.ProductService;
import org.hibernate.annotations.Parameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String showProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products"; // trả về file products.html
    }
    @GetMapping("/login_error")
    public String loginError(Model model, Authentication authentication) {
         authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities());
        model.addAttribute("errorMsg", "❌ Sai tài khoản hoặc mật khẩu!");
        return "login-error"; // => login-error.jsp
    }
    @GetMapping("/login")
    public String login() {
        return "login"; // => login.jsp
    }
    @PostMapping("/cart/doLogin")
    public String doLogin(Model model,@RequestParam("username") String name, @RequestParam("password")String pw){
        model.addAttribute("username",name);
        model.addAttribute("password",pw);
        return "login-error";
    }

}
