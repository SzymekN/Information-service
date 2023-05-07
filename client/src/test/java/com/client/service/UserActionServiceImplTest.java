package com.client.service;

import com.client.model.dto.UserRegistrationDto;
import com.client.model.entity.User;
import com.client.model.entity.UserDetails;
import com.client.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.ResourceAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserActionServiceImplTest {
    private UserActionServiceImpl userActionService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BasicServiceImpl basicService;
    @MockBean
    private PasswordEncoder passwordEncoder;
    private UserRegistrationDto userRegistrationDto;
    private HttpServletRequest request;
    private User user;
    @BeforeAll
    void setUp() {
        userRegistrationDto = mock(UserRegistrationDto.class);
        userActionService = new UserActionServiceImpl(userRepository, basicService, passwordEncoder);
        request = mock(HttpServletRequest.class);
        user = mock(User.class);
    }

    @Test
    void get_logged_user_should_return_logged_user() {
        // given
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String username = "testuser";
        User expectedUser = new User();
        expectedUser.setUsername(username);
        // when
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findUserByName(username)).thenReturn(expectedUser);
        // then
        Optional<User> actualUser = userActionService.getLoggedUser();
        verify(authentication).getName();
        verify(userRepository).findUserByName(username);
        assert(actualUser.isPresent());
        assert(actualUser.get().getUsername().equals(username));
        assert(actualUser.get().equals(expectedUser));
    }

    @Test
    void delete_client_to_editorial_should_call_delete_endpoint() {
        // given
        Long userId = 1L;
        // when
        when(basicService.copyHeadersFromRequest(any(HttpServletRequest.class))).thenReturn(new HttpHeaders());
        // then
        assertThrows(ResourceAccessException.class, () -> userActionService.deleteUserClientToEditorial(userId, request));
    }

    @Test
    void update_user_should_update_user_and_save() {
        // given & when
        when(userRegistrationDto.getUsername()).thenReturn("newuser");
        when(userRegistrationDto.getPassword()).thenReturn("newpassword");
        when(userRegistrationDto.getName()).thenReturn("newname");
        when(userRegistrationDto.getSurname()).thenReturn("newsurname");
        when(userRegistrationDto.getEmail()).thenReturn("newemail");
        when(passwordEncoder.encode(userRegistrationDto.getPassword())).thenReturn("hashedpassword");
        when(userRegistrationDto.getPassword()).thenReturn("newpassword");
        when(user.getUserDetails()).thenReturn(new UserDetails());
        // then
        userActionService.updateUser(user, userRegistrationDto);
        verify(user).setUsername("newuser");
        verify(user).setPassword("hashedpassword");
        verify(user, times(3)).getUserDetails();
        verify(userRepository).save(user);
    }

    @Test
    void update_user_client_to_editorial_should_update_user_and_return_response_entity() {
        // given
        Long userId = 1L;
        // when
        when(basicService.copyHeadersFromRequest(any(HttpServletRequest.class))).thenReturn(new HttpHeaders());
        // act
        assertThrows(HttpMessageConversionException.class, () -> userActionService.updateUserClientToEditorial(userId, userRegistrationDto, request));
    }
}
