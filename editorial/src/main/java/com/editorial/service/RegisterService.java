package com.editorial.service;

import com.editorial.model.dto.UserRegistrationDto;

public interface RegisterService {
    void registerUser(UserRegistrationDto userRegistrationDto);
    boolean checkIfUserExistsByEmail(String email);
}
