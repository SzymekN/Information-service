package com.client.service;

import com.client.model.dto.UserRegistrationDto;
import com.client.model.entity.User;
import com.client.model.entity.UserDetails;

public interface RegisterService {
    boolean checkIfUserExistsByUsername(String username);
    void registerUser(UserRegistrationDto userRegistrationDto);
    UserDetails dtoToUserDetails(UserRegistrationDto userRegistrationDto);
    User dtoToUser(UserRegistrationDto userRegistrationDto);
}
