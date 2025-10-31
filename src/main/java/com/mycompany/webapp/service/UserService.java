package com.mycompany.webapp.service;

import com.mycompany.webapp.entity.User;
import com.mycompany.webapp.entity.UserInfo;
import com.mycompany.webapp.repository.UserInfoRepository;
import com.mycompany.webapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

    public UserService(UserRepository userRepository, UserInfoRepository userInfoRepository) {
        this.userRepository = userRepository;
        this.userInfoRepository = userInfoRepository;
    }

    // --- User ---
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // --- UserInfo ---
    public UserInfo saveUserInfo(UserInfo info) {
        return userInfoRepository.save(info);
    }

    public Optional<UserInfo> findUserInfoById(Long id) {
        return userInfoRepository.findById(id);
    }

    public void deleteUserInfo(Long id) {
        userInfoRepository.deleteById(id);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


}
