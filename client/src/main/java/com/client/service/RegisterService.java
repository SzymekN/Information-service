package com.client.service;

import com.client.model.dto.UserRegistrationDto;
import com.client.model.entity.User;
import com.client.model.entity.UserDetails;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;

public interface RegisterService {
    boolean checkIfUserExistsByUsername(String username);
    void registerUser(UserRegistrationDto userRegistrationDto) throws MessagingException, UnsupportedEncodingException;
    UserDetails dtoToUserDetails(UserRegistrationDto userRegistrationDto);
    User dtoToUser(UserRegistrationDto userRegistrationDto);
    ResponseEntity<String> registerUserClientToEditorial(UserRegistrationDto userRegistrationDto, HttpServletRequest request);
    ResponseEntity<String> validateCode(String code, HttpServletRequest request);
}
