package com.online.shopping.auth.service;

import com.online.shopping.auth.dto.UserAddressRequest;
import com.online.shopping.auth.dto.UserRequest;
import com.online.shopping.auth.dto.UserResponse;
import com.online.shopping.auth.entity.User;
import com.online.shopping.auth.entity.UserAddress;
import com.online.shopping.auth.exception.UserAlreadyExist;
import com.online.shopping.auth.exception.UserNotFoundException;
import com.online.shopping.auth.repository.AddressRepo;
import com.online.shopping.auth.repository.LoginRepo;
import com.online.shopping.auth.repository.UserRepository;
import com.online.shopping.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    LoginRepo loginRepo;

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    JwtUtil jwtUtil;

    //signup
    public UserResponse signup(UserRequest userRequest) throws UserAlreadyExist {

        if (userRepository.existsByPhone(userRequest.getPhoneNumber())) {
            throw new UserAlreadyExist("Phone number already registered");
        }
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhoneNumber());

        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
        response.setPhoneNumber(savedUser.getPhone());
        return response;
    }

    // findBy userId
    public UserResponse findById(String authorizationHeader) throws UserNotFoundException {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UserNotFoundException("Invalid Authorization header");
        }

        // ✅ Remove "Bearer "
        String token = authorizationHeader.substring(7);
        UUID id = jwtUtil.extractUserId(token);
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            User user1 = user.get();
            UserResponse response = new UserResponse();
            response.setId(user1.getId());
            response.setName(user1.getName());
            response.setEmail(user1.getEmail());
            response.setPhoneNumber(user1.getPhone());
            System.out.print(response);
            return response;
        }

        throw new UserNotFoundException("User not found with id: " + id);
    }

    //updateById
    public UserResponse updateById(UUID id, UserRequest userRequest) throws UserNotFoundException {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        // Update basic fields
        if (userRequest.getName() != null) user.setName(userRequest.getName());
        if (userRequest.getEmail() != null) user.setEmail(userRequest.getEmail());
        if (userRequest.getPhoneNumber() != null) user.setPhone(userRequest.getPhoneNumber());
        if (userRequest.getDateOfBirth() != null) user.setDateOfBirth(userRequest.getDateOfBirth());
        if (userRequest.getGender() != null) user.setGender(userRequest.getGender());


        // Handle Address Update
        if (userRequest.getRequest() != null) {
            UserAddress newAddress = buildUserAddress(user, userRequest.getRequest());

            if (user.getUserAddresses() == null) {
                user.setUserAddresses(new ArrayList<>());
            }

            user.getUserAddresses().add(newAddress);
        }

        User updatedUser = userRepository.save(user);

        return buildUserResponse(updatedUser);
    }


    private UserResponse buildUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhone());

        return response;
    }


    //deleteById
    public String deleteById(UUID id) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(id);
            return "user delete successfully";
        }
        throw new UserNotFoundException("User not found with id: " + id);
    }

    public String addAddress(UserAddressRequest userRequest, UUID userId)
            throws UserNotFoundException {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found on this Id: " + userId));

        UserAddress address = buildUserAddress(user, userRequest);

        // initialize list if needed
        if (user.getUserAddresses() == null) {
            user.setUserAddresses(new ArrayList<>());
        }

        user.getUserAddresses().add(address);

        userRepository.save(user); // CASCADE saves address

        return "Address added successfully";
    }


    private UserAddress buildUserAddress(User user, UserAddressRequest addressRequest) {
        UserAddress newAddress = new UserAddress();

        newAddress.setAddressLine1(addressRequest.getAddressLine1());
        newAddress.setCity(addressRequest.getCity());
        newAddress.setState(addressRequest.getState());
        newAddress.setPostalCode(String.valueOf(addressRequest.getPostalCode()));
        newAddress.setCountry(addressRequest.getCountry());

        // set relationship
        newAddress.setUser(user);

        return newAddress;
    }

}
