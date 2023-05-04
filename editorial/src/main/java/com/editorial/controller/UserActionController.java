package com.editorial.controller;

import com.editorial.model.dto.UserRegistrationDto;
import com.editorial.model.entity.User;
import com.editorial.service.BasicServiceImpl;
import com.editorial.service.UserActionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/editorial/actions")
public class UserActionController {
    private final UserActionService userActionService;
    private final BasicServiceImpl basicService;
    @Autowired
    public UserActionController(UserActionService userActionService, BasicServiceImpl basicService) {
        this.userActionService = userActionService;
        this.basicService = basicService;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteUser(@RequestParam(name = "id") Long userId, HttpServletRequest request) {
        Optional<User> userChecker = userActionService.getLoggedUser();

        if(userChecker.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username of requesting user does not exist in db!");

        User loggedUser = userChecker.get();
        if (loggedUser.getAuthority().getAuthorityName().equals("ADMIN") || loggedUser.getId().equals(userId)) {
            userActionService.deleteUserById(userId);
            ResponseEntity<String> clientResponse = userActionService.deleteUserEditorialToClient(userId, request);
            if (!clientResponse.getStatusCode().is2xxSuccessful())
                return new ResponseEntity<>(clientResponse.getStatusCode());
            if (loggedUser.getId().equals(userId))
                return basicService.forceUserLogout();
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/delete/fc")
    public ResponseEntity<String> deleteUserForClient(@RequestParam(name = "id") Long userId, @RequestHeader("X-Caller") String caller) {
        if (!"DELETE_FROM_CLIENT".equals(caller))
            return ResponseEntity.badRequest().build();
        else {
            try {
                userActionService.deleteUserById(userId);
                return ResponseEntity.ok().build();
            } catch (Exception exception) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editUser(@RequestParam(name = "id") Long userId, @Valid @RequestBody UserRegistrationDto userRegistrationDto, HttpServletRequest request) {
        Optional<User> userChecker = userActionService.getLoggedUser();

        if(userChecker.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username of requesting user does not exist in db!");

        User loggedUser = userChecker.get();
        if (loggedUser.getAuthority().getAuthorityName().equals("ADMIN") || loggedUser.getId().equals(userId)) {
            User userToEdit = userActionService.findUserById(userId);
            userActionService.updateUser(userToEdit, userRegistrationDto);
            ResponseEntity<String> clientResponse = userActionService.updateUserEditorialToClient(userId, userRegistrationDto, request);
            if (!clientResponse.getStatusCode().is2xxSuccessful())
                return ResponseEntity.badRequest().body("Bad request from client side - editorial");
            return ResponseEntity.ok("Successfully edited user");
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Insufficient privileges - you are neither admin nor account owner!");
    }

    @PutMapping("/edit/fc")
    public ResponseEntity<String> editUserForClient(@RequestParam(name = "id") Long userId, @Valid @RequestBody UserRegistrationDto userRegistrationDto,
                                                    @RequestHeader("X-Caller") String caller) {
        if (!"EDIT_FROM_CLIENT".equals(caller))
            return ResponseEntity.badRequest().build();
        else {
            try {
                User userToEdit = userActionService.findUserById(userId);
                userActionService.updateUser(userToEdit, userRegistrationDto);
                return ResponseEntity.ok().build();
            } catch (Exception exception) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }
}
