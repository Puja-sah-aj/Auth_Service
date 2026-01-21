package com.online.shopping.auth.service;


import com.online.shopping.auth.dto.LoginRequest;
import com.online.shopping.auth.dto.OtpVerificationRequest;
import com.online.shopping.auth.entity.LoginEntity;
import com.online.shopping.auth.entity.User;
import com.online.shopping.auth.repository.LoginRepo;
import com.online.shopping.auth.repository.UserRepository;
import com.online.shopping.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class LoginService {

    @Autowired
    LoginRepo loginRepo;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;

    public String login(LoginRequest loginRequest) {
        Optional<User> existingUser = userRepository.findByPhone(loginRequest.getPhoneNumber());
        User user;
        LoginEntity loginEntity;

        if (existingUser.isPresent()) {
            user = existingUser.get();
            loginEntity = user.getLogin();
            if (loginEntity == null) {
                loginEntity = new LoginEntity();
                loginEntity.setUser(user);
                user.setLogin(loginEntity);
            }
        } else {
            user = new User();
            user.setPhone(loginRequest.getPhoneNumber());
            
            loginEntity = new LoginEntity();
            loginEntity.setPhoneNumber(loginRequest.getPhoneNumber()); // Keeping for redundancy/legacy
            loginEntity.setUser(user);
            
            user.setLogin(loginEntity);
        }

        String otp = generateOtp();
        loginEntity.setOtp(otp);
        loginEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        
        // Saving the user will cascade and save the loginEntity because of CascadeType.ALL
        userRepository.save(user);
        System.out.println(otp);

        // For testing purposes, returning the OTP in the response
        return "OTP sent to " + loginRequest.getPhoneNumber() + ": " + otp;
    }

    private String generateOtp() {
        Random random = new Random();
        int otpValue = 1000 + random.nextInt(9000);
        return String.valueOf(otpValue);
    }

    public String verifyOtp(OtpVerificationRequest verificationRequest) {
        Optional<User> userOptional = userRepository.findByPhone(verificationRequest.getPhoneNumber());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            LoginEntity loginEntity = user.getLogin();

            if (loginEntity != null && loginEntity.getOtp() != null && loginEntity.getOtp().equals(verificationRequest.getOtp())) {
                if (loginEntity.getExpiryTime().isAfter(LocalDateTime.now())) {
                    // OTP is valid and not expired
                    // Clear OTP after successful verification
                    loginEntity.setOtp(null);
                    loginEntity.setExpiryTime(null);
                    userRepository.save(user);

                    // Pass the LoginEntity to generateToken, as it expects it
                    return jwtUtil.generateToken(loginEntity);
                } else {
                    throw new RuntimeException("OTP has expired");
                }
            } else {
                throw new RuntimeException("Invalid OTP");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }


    public void logout(String token) {
        // For a stateless JWT implementation, the client is responsible for
        // deleting the token. If you want to enforce server-side invalidation,
        // you would implement a token blacklist here. For example, you could
        // cache the token with its expiration date until it naturally expires.
        System.out.println("Logout request received. Client should discard token: " + token);
    }
}
