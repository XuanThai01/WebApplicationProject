package com.mycompany.webapp.service;

import com.mycompany.webapp.entity.UserInfo;
import com.mycompany.webapp.entity.Voucher;
import com.mycompany.webapp.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserInfoService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    public Set<Voucher> getVouchersByUserId(Long userId) {
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return user.getVouchers(); // lấy trực tiếp từ entity
    }


    public void save(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }
}