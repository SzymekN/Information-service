package com.client.security;

import com.client.model.entity.User;
import com.client.repository.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@NoArgsConstructor
public class ClientDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public ClientDetailsService(UserRepository userRepository){
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
