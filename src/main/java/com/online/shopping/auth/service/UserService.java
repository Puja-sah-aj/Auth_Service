package com.online.shopping.auth.service;

import com.online.shopping.auth.dto.UserRequest;
import com.online.shopping.auth.dto.UserResponse;
import com.online.shopping.auth.entity.User;
import com.online.shopping.auth.exception.UserAlreadyExist;
import com.online.shopping.auth.exception.UserNotFoundException;
import com.online.shopping.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //signup
    public UserResponse signup(UserRequest userRequest)throws UserAlreadyExist{

        if (userRepository.existsByPhoneNumber(userRequest.getPhoneNumber())) {
            throw new UserAlreadyExist("Phone number already registered");
        }
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());

        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
        response.setPhoneNumber(savedUser.getPhoneNumber());
        return  response;
    }

    // findBy userId
    public UserResponse findById(UUID id)throws UserNotFoundException {

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            User user1 = user.get();
            UserResponse response = new UserResponse();
            response.setId(user1.getId());
            response.setName(user1.getName());
            response.setEmail(user1.getEmail());
            response.setPhoneNumber(user1.getPhoneNumber());

            return response;
        }

        throw new UserNotFoundException("User not found with id: " + id);
    }

//updateById
    public UserResponse updateById(UUID id, UserRequest userRequest) throws UserNotFoundException{
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setName(userRequest.getName());
            user.setEmail(userRequest.getEmail());
            user.setPhoneNumber(userRequest.getPhoneNumber());

            User updatedUser = userRepository.save(user);

            UserResponse response = new UserResponse();
            response.setId(updatedUser.getId());
            response.setName(updatedUser.getName());
            response.setEmail(updatedUser.getEmail());
            response.setPhoneNumber(updatedUser.getPhoneNumber());

            return response;
        }
        throw new UserNotFoundException("User not found with id: " + id);
    }

    //deleteById
    public String deleteById(UUID id)throws UserNotFoundException{
        Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                userRepository.deleteById(id);
                return "user delete successfully";
            }
        throw new UserNotFoundException("User not found with id: " + id);
    }
}
