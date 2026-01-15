package com.online.shopping.auth.controller;

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
    @GetMapping("/userId/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id)throws UserNotFoundException {
        return ResponseEntity.ok(userService.findById(id));
    }

    //updateById
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> updateById(@PathVariable UUID id,@RequestBody UserRequest userRequest)throws UserNotFoundException{
        return ResponseEntity.ok(userService.updateById(id,userRequest));
    }

    //deleteById
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable UUID id)throws UserNotFoundException{
        return ResponseEntity.ok(userService.deleteById(id));
    }

}
