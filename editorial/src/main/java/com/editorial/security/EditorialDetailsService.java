package com.editorial.security;

import com.editorial.model.entity.User;
import com.editorial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EditorialDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public EditorialDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = Optional.of(userRepository.findUserByName(username)).orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getAuthority().getAuthorityName())
                .build();
    }
}
