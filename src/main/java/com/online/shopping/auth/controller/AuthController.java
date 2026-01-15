package com.online.shopping.auth.controller;


import com.online.shopping.auth.dto.LoginRequest;
import com.online.shopping.auth.dto.OtpVerificationRequest;
import com.online.shopping.auth.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){
        String otp = loginService.login(loginRequest);
        return ResponseEntity.ok("OTP sent successfully!"+otp);
    }
    
    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest verificationRequest) {
        String result = loginService.verifyOtp(verificationRequest);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        // The token will be in the format "Bearer <token>", so we need to extract it.
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            loginService.logout(jwt);
            return ResponseEntity.ok("Logged out successfully. Please discard your token.");
        }
        return ResponseEntity.badRequest().body("Invalid Authorization header.");
    }
}
