package com.asrez.wheremoney.api.service;

import com.asrez.wheremoney.api.entity.User;
import com.asrez.wheremoney.api.exception.ApiRequestException;
import com.asrez.wheremoney.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User addUser(User user) {
        if (userRepository.existsByUsername(user.getUsername()))
            throw new ApiRequestException("Username exists!", "USERNAME_EXISTS");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("ROLE_USER"));

        userRepository.save(user);
        return user;
    }

    public User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty())
            throw new ApiRequestException("User not found!", "USER_NOT_FOUND");

        return user.get();
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Map<Object, Object> deleteUser(Long id) {
        if (!userRepository.existsById(id))
            throw new ApiRequestException("User not found!", "USER_NOT_FOUND");

        userRepository.deleteById(id);
        Map<Object, Object> model = new HashMap<>();
        model.put("id", id);
        model.put("success", true);
        return model;
    }

    public User updateUser(Long id, User user) {
        if (!userRepository.existsById(id))
            throw new ApiRequestException("User not found!", "USER_NOT_FOUND");

        String userName = user.getUsername();
        if (userName != null && userRepository.existsByUsername(userName))
            throw new ApiRequestException("Username exists!", "USERNAME_EXISTS");

        User oldUser = userRepository.findById(id).get();
        if (user.getName() != null)
            oldUser.setName(user.getName());
        if (user.getFamilyName() != null)
            oldUser.setFamilyName(user.getFamilyName());
        if (user.getUsername() != null)
            oldUser.setUsername(userName);
        if (user.getPassword() != null)
            oldUser.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(oldUser);
        return oldUser;
    }

}
