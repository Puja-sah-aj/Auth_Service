package com.online.shopping.auth.service;

import com.online.shopping.auth.dto.UserAddressRequest;
import com.online.shopping.auth.dto.UserAddressResponse;
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
import java.util.List;
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

//        if (userRepository.existsByPhone(userRequest.getPhoneNumber())) {
//            throw new UserAlreadyExist("Phone number already registered");
//        }
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
       // user.setPhone(userRequest.getPhoneNumber());

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

        String token = authorizationHeader.substring(7);
        UUID id = jwtUtil.extractUserId(token);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhone());
        response.setGender(user.getGender());
        response.setDateOfBirth(user.getDateOfBirth());

        List<UserAddressResponse> addressResponses = new ArrayList<>();

        for (UserAddress address : user.getUserAddresses()) {
            UserAddressResponse ar = new UserAddressResponse();
            ar.setId(address.getId());
            ar.setAddressLine1(address.getAddressLine1());
            ar.setAddressLine2(address.getAddressLine2());
            ar.setCity(address.getCity());
            ar.setState(address.getState());
            ar.setPostalCode(address.getPostalCode());
            ar.setCountry(address.getCountry());
            addressResponses.add(ar);
        }

        response.setUserAddress(addressResponses);
        System.out.print(response);
        return response;
    }


    //updateById
    public String updateById(UUID id, UserRequest userRequest) throws UserNotFoundException {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        // Update basic fields
        if (userRequest.getName() != null) user.setName(userRequest.getName());
        if (userRequest.getEmail() != null) user.setEmail(userRequest.getEmail());
        //if (userRequest.getPhoneNumber() != null) user.setPhone(userRequest.getPhoneNumber());
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

        return "updated successfully";
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

    public UserAddressResponse addAddress(UserAddressRequest userRequest, UUID userId)
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

        userRepository.saveAndFlush(user);
        UserAddress savedAddress =
                user.getUserAddresses().get(user.getUserAddresses().size() - 1);

        UserAddressResponse response = new UserAddressResponse();
        response.setId(savedAddress.getId());
        response.setAddressLine1(savedAddress.getAddressLine1());
        response.setAddressLine2(savedAddress.getAddressLine2());
        response.setCity(savedAddress.getCity());
        response.setState(savedAddress.getState());
        response.setPostalCode(savedAddress.getPostalCode());
        response.setCountry(savedAddress.getCountry());
        System.out.print(response);
        return response;

    }

    public UserAddressResponse getAddressById(UUID addressId) throws UserNotFoundException {
        UserAddress address = addressRepo.findById(addressId)
                .orElseThrow(() -> new UserNotFoundException("Address not found with id: " + addressId));

        UserAddressResponse response = new UserAddressResponse();
        response.setId(address.getId());
        response.setAddressLine1(address.getAddressLine1());
        response.setAddressLine2(address.getAddressLine2());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setPostalCode(address.getPostalCode());
        response.setCountry(address.getCountry());
        return response;
    }


    private UserAddress buildUserAddress(User user, UserAddressRequest addressRequest) {
        UserAddress newAddress = new UserAddress();

        newAddress.setAddressLine1(addressRequest.getAddressLine1());
        newAddress.setAddressLine2(addressRequest.getAddressLine2());
        newAddress.setCity(addressRequest.getCity());
        newAddress.setState(addressRequest.getState());
        newAddress.setPostalCode(String.valueOf(addressRequest.getPostalCode()));
        newAddress.setCountry(addressRequest.getCountry());

        // set relationship
        newAddress.setUser(user);

        return newAddress;
    }

}
