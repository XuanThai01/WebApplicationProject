package com.mycompany.webapp.security;

import com.mycompany.webapp.filter.CheckCookieFilter;
import com.mycompany.webapp.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private final CustomCartMergeHandler successHandler;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomCartMergeHandler successHandler,
                          CustomUserDetailsService userDetailsService
    ) {
        this.successHandler = successHandler;
        this.userDetailsService = userDetailsService;

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/cart/**","/images/**","/imagesFolder/**","/listpd/**","/creat_cart/**","/page-cart","/updateCart","/updateCartQty","/register/**","/api/products/search","/login-page","/creatOder","/submitOrder","/orders/save","/page-order-detail","/orders/delete","/cart/delete","/manage/**","/checkout","/fakepay/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login-page")
                        .loginProcessingUrl("/doLogin")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler((req, res, auth) -> {
                            successHandler.mergeGuestCartToUser(req, auth);
                            res.setContentType("application/json");
                            res.getWriter().write("{\"status\":\"ok\"}");
                        })
                        .failureHandler((req, res, ex) -> {
                            res.setContentType("application/json");
                            res.getWriter().write("{\"status\":\"fail\",\"message\":\"Sai tài khoản hoặc mật khẩu\"}");
                        })
                )
                .logout(logout -> logout.permitAll())
                .authenticationManager(authManager(http))
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())  // Cho phép iframe cùng domain
                );
        http.addFilterAfter(new CheckCookieFilter(),
                org.springframework.security.web.context.SecurityContextPersistenceFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        // Cấu hình UserDetailsService và PasswordEncoder
        authBuilder.userDetailsService(userDetailsService);
        //  .passwordEncoder(this.passwordEncoder()); (dùng khi muốn chỉ định so sánh loại mật khẩu cụ thể với db, ko dùng sẽ có mặc định , chỉ định mật khẩu trong db là {id}mk, (vd:{noop}mk đối với mặc định để spring hiểu là dùng loại nào))

        // Build AuthenticationManager từ AuthenticationManagerBuilder
        return authBuilder.build();
    }

}
