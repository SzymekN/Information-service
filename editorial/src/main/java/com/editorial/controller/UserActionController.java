package com.editorial.controller;

import com.editorial.model.entity.User;
import com.editorial.repository.UserRepository;
import com.editorial.service.BasicServiceImpl;
import com.editorial.service.UserActionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/editorial/actions")
public class UserActionController {
    private final UserActionService userActionService;
    private final UserRepository userRepository;
    private final BasicServiceImpl basicService;
    @Autowired
    public UserActionController(UserActionService userActionService, UserRepository userRepository, BasicServiceImpl basicService) {
        this.userActionService = userActionService;
        this.userRepository = userRepository;
        this.basicService = basicService;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteUser(@RequestParam(name = "id") Long userId, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userChecker = Optional.ofNullable(userRepository.findUserByName(authentication.getName()));

        if(userChecker.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username of requesting user does not exist in db!");

        User loggedUser = userChecker.get();
        if (loggedUser.getAuthority().getAuthorityName().equals("ADMIN") || loggedUser.getId().equals(userId)) {
            try {
                userRepository.deleteUserById(userId);
                ResponseEntity<String> clientResponse = userActionService.deleteUserEditorialToClient(userId, request);
                if (!clientResponse.getStatusCode().is2xxSuccessful())
                    return ResponseEntity.badRequest().body("Bad request from client side - editorial");
                if (loggedUser.getId().equals(userId))
                    return basicService.forceUserLogout();
            } catch (Exception exception) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/delete/fc")
    public ResponseEntity<String> deleteUserForEditorial(@RequestParam(name = "id") Long userId, @RequestHeader("X-Caller") String caller) {
        System.out.println(caller);
        if (!"DELETE_FROM_CLIENT".equals(caller))
            return ResponseEntity.badRequest().build();
        else {
            try {
                userRepository.deleteUserById(userId);
                return ResponseEntity.ok().build();
            } catch (Exception exception) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }
}
