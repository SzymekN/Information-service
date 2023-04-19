package com.client.security;

import com.client.model.entity.User;
import com.client.repository.UserRepository;
import com.client.util.ExternalAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public ClientDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, ExternalAuthenticationException {
        User user = Optional.of(userRepository.findUserByName(username)).orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
        if(!"APP".equals(user.getUserDetails().getSupplier())) {
            throw new ExternalAuthenticationException(user.getUserDetails().getSupplier());
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getAuthority().getAuthorityName())
                .build();
    }
}
