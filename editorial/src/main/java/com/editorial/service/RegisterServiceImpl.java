package com.editorial.service;

import com.editorial.model.dto.UserRegistrationDto;
import com.editorial.model.entity.Authority;
import com.editorial.model.entity.User;
import com.editorial.model.entity.UserDetails;
import com.editorial.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService{

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public RegisterServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public boolean checkIfUserExistsByEmail(String email) {
        return userRepository.existsUsersByEmail(email);
    }

    @Override
    public void registerUser(UserRegistrationDto userRegistrationDto) {
        User user = dtoToUser(userRegistrationDto);
        UserDetails userDetails = dtoToUserDetails(userRegistrationDto);
        Authority authority = new Authority(userRegistrationDto.getAuthorityName());
        user.connectUserDetails(userDetails);
        userDetails.connectUser(user);
        user.connectAuthority(authority);
        authority.setUser(user);
        userRepository.save(user);
    }

    private UserDetails dtoToUserDetails(UserRegistrationDto userRegistrationDto){
        return UserDetails.builder()
                .name(userRegistrationDto.getName())
                .surname(userRegistrationDto.getSurname())
                .email(userRegistrationDto.getEmail())
                .supplier(userRegistrationDto.getSupplier())
                .build();
    }

    private User dtoToUser(UserRegistrationDto userRegistrationDto){
        return User.builder()
                .username(userRegistrationDto.getUsername())
                .password(passwordEncoder.encode(userRegistrationDto.getPassword()))
                .enabled(true)
                .build();
    }
}
