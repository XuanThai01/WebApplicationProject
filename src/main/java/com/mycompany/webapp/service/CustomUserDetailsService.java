package com.mycompany.webapp.service;

import com.mycompany.webapp.entity.User;
import com.mycompany.webapp.repository.UserRepository;
import com.mycompany.webapp.security.MyUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(">>> [DEBUG] CustomUserDetailsService.loadUserByUsername called with username = " + username);

        User user = userRepository.findByUsername(username);
             //   .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        if (user == null) {
            System.out.println("  "+username);
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return new MyUserDetails(user);

    }
}
