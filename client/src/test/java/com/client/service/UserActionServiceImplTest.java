package com.client.service;

import com.client.model.dto.UserDto;
import com.client.model.dto.UserRegistrationDto;
import com.client.model.entity.Authority;
import com.client.model.entity.User;
import com.client.model.entity.UserDetails;
import com.client.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private User user2;
    private Pageable pageable;
    private Slice<User> pagedUsers;
    @BeforeAll
    void setUp() {
        userRegistrationDto = mock(UserRegistrationDto.class);
        userActionService = new UserActionServiceImpl(userRepository, basicService, passwordEncoder);
        request = mock(HttpServletRequest.class);
        user = mock(User.class);
        pageable = mock(Pageable.class);
        pagedUsers = mock(Slice.class);

        Authority auth = Authority.builder()
                .id(Long.parseLong("1"))
                .authorityName("USER")
                .build();

        UserDetails userDetails = UserDetails.builder()
                .id(Long.parseLong("1"))
                .name("Test")
                .surname("TestSurname")
                .email("Test1@email.com")
                .supplier("APP")
                .build();

        user2 = User.builder()
                .id(Long.parseLong("1"))
                .password("user")
                .username("user")
                .enabled(true)
                .authority(auth)
                .userDetails(userDetails)
                .endorsements(new ArrayList<>())
                .build();
        auth.setUser(user2);
        userDetails.setUser(user2);
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
        assertThrows(ResourceAccessException.class, () -> userActionService.updateUserClientToEditorial(userId, userRegistrationDto, request));
    }

    @Test
    void find_all_users_paged_should_return_no_content_when_no_users_exist() {
        // given
        when(pagedUsers.hasContent()).thenReturn(false);
        when(userRepository.findAllPaged(pageable)).thenReturn(pagedUsers);

        // when
        ResponseEntity<List<UserDto>> response = userActionService.findAllUsersPaged(pageable);

        // then
        verify(userRepository).findAllPaged(pageable);
        assertEquals(ResponseEntity.noContent().build(), response);
    }

    @Test
    void find_all_users_paged_should_return_users_and_headers_when_users_exist() {
        // given
        List<User> users = new ArrayList<>();
        users.add(user2);
        when(pagedUsers.hasContent()).thenReturn(true);
        when(pagedUsers.getContent()).thenReturn(users);
        when(userRepository.findAllPaged(pageable)).thenReturn(pagedUsers);
        when(userRepository.countAllUsers()).thenReturn(1L);

        // when
        ResponseEntity<List<UserDto>> response = userActionService.findAllUsersPaged(pageable);

        // then
        verify(userRepository).findAllPaged(pageable);
        verify(userRepository).countAllUsers();
        HttpHeaders expectedHeaders = new HttpHeaders();
        expectedHeaders.set("X-Total-Count", "1");
        assertEquals(ResponseEntity.ok().headers(expectedHeaders).body(userActionService.usersToDto(users)), response);
    }

    @Test
    void find_all_users_by_role_paged_should_return_users_and_headers_when_users_exist() {
        // given
        String role = "ADMIN";
        List<User> users = new ArrayList<>();
        users.add(user2);
        when(pagedUsers.hasContent()).thenReturn(true);
        when(pagedUsers.getContent()).thenReturn(users);
        when(userRepository.findAllByRolePaged(pageable, role)).thenReturn(pagedUsers);
        when(userRepository.countAllByRole(role)).thenReturn(1L);

        // when
        ResponseEntity<List<UserDto>> response = userActionService.findAllUsersByRolePaged(pageable, role);

        // then
        verify(userRepository).findAllByRolePaged(pageable, role);
        verify(userRepository).countAllByRole(role);
        HttpHeaders expectedHeaders = new HttpHeaders();
        expectedHeaders.set("X-Total-Count", "1");
        assertEquals(ResponseEntity.ok().headers(expectedHeaders).body(userActionService.usersToDto(users)), response);
    }

    @Test
    void find_all_users_by_field_paged_should_return_users_and_headers_when_users_exist() {
        // given
        String field = "name";
        String value = "Test";
        List<User> users = new ArrayList<>();
        users.add(user2);
        when(pagedUsers.hasContent()).thenReturn(true);
        when(pagedUsers.getContent()).thenReturn(users);
        when(userRepository.findAllByNamePaged(pageable, value)).thenReturn(pagedUsers);
        when(userRepository.countAllByName(value)).thenReturn(1L);

        // when
        ResponseEntity<List<UserDto>> response = userActionService.findAllUsersByFieldPaged(pageable, field, value);

        // then
        verify(userRepository).findAllByNamePaged(pageable, value);
        verify(userRepository).countAllByName(value);
        HttpHeaders expectedHeaders = new HttpHeaders();
        expectedHeaders.set("X-Total-Count", "1");
        assertEquals(ResponseEntity.ok().headers(expectedHeaders).body(userActionService.usersToDto(users)), response);
    }

    @Test
    void find_all_users_by_field_and_role_paged_should_return_users_and_headers_when_users_exist() {
        // given
        String role = "USER";
        String field = "name";
        String value = "Test";
        List<User> users = new ArrayList<>();
        users.add(user2);
        when(pagedUsers.hasContent()).thenReturn(true);
        when(pagedUsers.getContent()).thenReturn(users);
        when(userRepository.findAllByNameAndRolePaged(pageable, value, role)).thenReturn(pagedUsers);
        when(userRepository.countAllByNameAndRole(value, role)).thenReturn(1L);

        // when
        ResponseEntity<List<UserDto>> response = userActionService.findAllUsersByFieldAndRolePaged(pageable, role, field, value);

        // then
        verify(userRepository).findAllByNameAndRolePaged(pageable, value, role);
        verify(userRepository).countAllByNameAndRole(value, role);
        HttpHeaders expectedHeaders = new HttpHeaders();
        expectedHeaders.set("X-Total-Count", "1");
        assertEquals(ResponseEntity.ok().headers(expectedHeaders).body(userActionService.usersToDto(users)), response);
    }

    @Test
    void usersToDto_should_convert_users_to_userDtos() {
        // given
        List<User> users = new ArrayList<>();
        users.add(user2);

        // when
        List<UserDto> dtos = userActionService.usersToDto(users);

        // then
        assertEquals(1, dtos.size());

        UserDto dto = dtos.get(0);
        assertEquals(user2.getId(), dto.getId());
        assertEquals(user2.getUsername(), dto.getUsername());
        assertEquals(user2.getUserDetails().getName(), dto.getName());
        assertEquals(user2.getUserDetails().getSurname(), dto.getSurname());
        assertEquals(user2.getUserDetails().getEmail(), dto.getEmail());
        assertEquals(user2.getAuthority().getAuthorityName(), dto.getAuthorityName());
    }
}
