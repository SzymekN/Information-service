package com.editorial.service;

import com.editorial.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface UserActionService {
    User getLoggedUser();
    ResponseEntity<String> deleteUserEditorialToClient(Long userId, HttpServletRequest request);
}
