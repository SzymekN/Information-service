package com.editorial.service;

import com.editorial.model.dto.UserEditDto;
import com.editorial.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserActionService {
    Optional<User> getLoggedUser();
    ResponseEntity<String> deleteUserEditorialToClient(Long userId, HttpServletRequest request);
    void updateUser(User user, UserEditDto userEditDto, Long loggedUserId);
    ResponseEntity<String> updateUserEditorialToClient(Long userId, Long loggedUserId, UserEditDto userEditDto, HttpServletRequest servletRequest);
    void deleteUserById(Long id);
    User findUserById(Long id);
}
