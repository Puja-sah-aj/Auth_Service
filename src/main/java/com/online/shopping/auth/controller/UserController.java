package com.online.shopping.auth.controller;

import com.online.shopping.auth.dto.UserAddressRequest;
import com.online.shopping.auth.dto.UserAddressResponse;
import com.online.shopping.auth.dto.UserRequest;
import com.online.shopping.auth.dto.UserResponse;
import com.online.shopping.auth.exception.UserAlreadyExist;
import com.online.shopping.auth.exception.UserNotFoundException;
import com.online.shopping.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;
  //signup
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody UserRequest userRequest) throws UserAlreadyExist {
        return ResponseEntity.ok(userService.signup(userRequest));
    }
   //findById
    @GetMapping("/me")
    public ResponseEntity<UserResponse> findById(@RequestHeader("Authorization") String authorizationHeader)throws UserNotFoundException {
        return ResponseEntity.ok(userService.findById(authorizationHeader));
    }

    //updateById
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateById(@PathVariable UUID id,@RequestBody UserRequest userRequest)throws UserNotFoundException{
        return ResponseEntity.ok(userService.updateById(id,userRequest));
    }

    @PostMapping("/address/{userId}")
    public ResponseEntity<UserAddressResponse> addAddress(@RequestBody UserAddressRequest userRequest, @PathVariable UUID userId)throws UserNotFoundException{
        return ResponseEntity.ok(userService.addAddress(userRequest,userId));
    }

    @GetMapping("/address/{addressId}")
    public ResponseEntity<UserAddressResponse> getAddressById(@PathVariable UUID addressId) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getAddressById(addressId));
    }

    //deleteById
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable UUID id)throws UserNotFoundException{
        return ResponseEntity.ok(userService.deleteById(id));
    }

}
