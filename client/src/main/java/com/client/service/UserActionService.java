package com.client.service;

import com.client.model.dto.UserRegistrationDto;
import com.client.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserActionService {
    Optional<User> getLoggedUser();
    ResponseEntity<String> deleteUserClientToEditorial(Long userId, HttpServletRequest request);
    void updateUser(User user, UserRegistrationDto userRegistrationDto);
    ResponseEntity<String> updateUserClientToEditorial(Long userId, UserRegistrationDto userRegistrationDto, HttpServletRequest servletRequest);
    void deleteUserById(Long id);
    User findUserById(Long userId);
}
