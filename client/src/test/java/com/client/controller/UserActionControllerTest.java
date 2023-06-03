package com.client.controller;

import com.client.model.dto.UserDto;
import com.client.model.dto.UserEditDto;
import com.client.model.entity.Authority;
import com.client.model.entity.User;
import com.client.model.entity.UserDetails;
import com.client.repository.UserRepository;
import com.client.security.SecurityConfig;
import com.client.service.BasicServiceImpl;
import com.client.service.UserActionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserActionController.class)
@OverrideAutoConfiguration(enabled = true)
@ActiveProfiles("testmvc")
@Import(SecurityConfig.class)
public class UserActionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserActionService userActionService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BasicServiceImpl basicService;

    @Test
    @WithMockUser(roles = "USER")
    void get_user_info_should_return_user_info_when_user_is_logged_in() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        UserDetails userDetails = new UserDetails();
        userDetails.setName("John");
        userDetails.setSurname("Doe");
        userDetails.setEmail("john.doe@example.com");
        userDetails.setSupplier("APP");
        user.setUserDetails(userDetails);

        UserDto expectedUserDto = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getUserDetails().getName())
                .surname(user.getUserDetails().getSurname())
                .email(user.getUserDetails().getEmail())
                .supplier(user.getUserDetails().getSupplier())
                .build();

        when(userActionService.getLoggedUser()).thenReturn(Optional.of(user));
        when(userActionService.getUserInfo(any(User.class))).thenReturn(expectedUserDto);

        // when
        mockMvc.perform(get("/client/actions/user/info").with(csrf()))
        // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedUserDto.getId()))
                .andExpect(jsonPath("$.username").value(expectedUserDto.getUsername()))
                .andExpect(jsonPath("$.name").value(expectedUserDto.getName()))
                .andExpect(jsonPath("$.surname").value(expectedUserDto.getSurname()))
                .andExpect(jsonPath("$.email").value(expectedUserDto.getEmail()))
                .andExpect(jsonPath("$.supplier").value(expectedUserDto.getSupplier()));

        verify(userActionService, times(1)).getLoggedUser();
        verify(userActionService, times(1)).getUserInfo(any(User.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void get_user_info_should_return_unauthorized_when_user_is_not_logged_in() throws Exception {
        // given
        when(userActionService.getLoggedUser()).thenReturn(Optional.empty());

        // when
        mockMvc.perform(get("/client/actions/user/info").with(csrf()))
        // then
                .andExpect(status().isUnauthorized());

        verify(userActionService, times(1)).getLoggedUser();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void delete_user_for_admin() throws Exception {
        // given
        Long userId = 1L;
        HttpServletRequest request = mock(HttpServletRequest.class);
        User loggedUser = new User();
        loggedUser.setId(2L);
        loggedUser.setAuthority(new Authority("ADMIN"));
        // when
        when(userActionService.getLoggedUser()).thenReturn(Optional.of(loggedUser));
        doNothing().when(userRepository).deleteUserById(userId);
        when(userActionService.deleteUserClientToEditorial(any(Long.class), any(HttpServletRequest.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(basicService.forceUserLogout()).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        // then
        mockMvc.perform(delete("/client/actions/delete").with(csrf())
                        .param("id", userId.toString())
                        .requestAttr("request", request))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void delete_user_for_user() throws Exception {
        // given
        Long userId = 1L;
        HttpServletRequest request = mock(HttpServletRequest.class);
        User loggedUser = new User();
        loggedUser.setId(1L);
        loggedUser.setAuthority(new Authority("USER"));
        // when
        when(userActionService.getLoggedUser()).thenReturn(Optional.of(loggedUser));
        doNothing().when(userRepository).deleteUserById(userId);
        when(userActionService.deleteUserClientToEditorial(any(Long.class), any(HttpServletRequest.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(basicService.forceUserLogout()).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        // then
        mockMvc.perform(delete("/client/actions/delete").with(csrf())
                        .param("id", userId.toString())
                        .requestAttr("request", request))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void delete_user_no_admin() throws Exception {
        // given
        Long userId = 1L;
        HttpServletRequest request = mock(HttpServletRequest.class);
        User usingUser = new User();
        usingUser.setId(2L);
        usingUser.setAuthority(new Authority("USER"));
        // when
        when(userActionService.getLoggedUser()).thenReturn(Optional.of(usingUser));
        doNothing().when(userRepository).deleteUserById(userId);
        when(userActionService.deleteUserClientToEditorial(any(Long.class), any(HttpServletRequest.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(basicService.forceUserLogout()).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        // then
        mockMvc.perform(delete("/client/actions/delete").with(csrf())
                        .param("id", userId.toString())
                        .requestAttr("request", request))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void delete_user_for_editorial() throws Exception {
        // given
        Long userId = 1L;
        String caller = "DELETE_FROM_EDITORIAL";
        // when
        doNothing().when(userRepository).deleteUserById(userId);
        // then
        mockMvc.perform(delete("/client/actions/delete/fe").with(csrf())
                        .param("id", userId.toString())
                        .header("X-Caller", caller))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void edit_user() throws Exception {
        // given
        Long userId = 1L;
        UserEditDto userEditDto = new UserEditDto();
        HttpServletRequest request = mock(HttpServletRequest.class);
        User loggedUser = new User();
        loggedUser.setId(1L);
        loggedUser.setAuthority(new Authority("ADMIN"));
        User userToEdit = new User();
        userToEdit.setUsername("testt");
        userEditDto.setName("name");
        userEditDto.setSurname("name");
        userEditDto.setUsername("test");
        userEditDto.setAuthorityName("USER");
        // when
        when(userActionService.getLoggedUser()).thenReturn(Optional.of(loggedUser));
        when(userRepository.findUserById(userId)).thenReturn(userToEdit);
        when(userActionService.findUserById(userId)).thenReturn(userToEdit);
        when(userRepository.findUserByName(anyString())).thenReturn(User.builder().username(null).build());
        when(userActionService.updateUserClientToEditorial(any(Long.class), any(Long.class), any(UserEditDto.class),
                any(HttpServletRequest.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        // then
        mockMvc.perform(put("/client/actions/edit").with(csrf())
                        .param("id", userId.toString())
                        .content(new ObjectMapper().writeValueAsString(userEditDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("request", request))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("Successfully edited user", result.getResponse().getContentAsString()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void edit_user_invalid_input() throws Exception {
        // given
        Long userId = 1L;
        UserEditDto userEditDto = new UserEditDto();
        HttpServletRequest request = mock(HttpServletRequest.class);
        userEditDto.setName("name");
        userEditDto.setSurname("name23");
        userEditDto.setUsername("test");
        // when & then
        mockMvc.perform(put("/client/actions/edit").with(csrf())
                        .param("id", userId.toString())
                        .content(new ObjectMapper().writeValueAsString(userEditDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("request", request))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "NOTALLOWED")
    public void for_not_allowed_user() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        // when & then
        mockMvc.perform(delete("/client/actions/delete").with(csrf())
                        .param("id", "1")
                        .requestAttr("request", request))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void get_users_info_paged_should_return_users_when_role_and_attribute_name_provided() throws Exception {
        // given
        String role = "ADMIN";
        String attributeName = "username";
        String attributeValue = "test";
        List<UserDto> userDtos = new ArrayList<>();
        when(userActionService.getLoggedUser()).thenReturn(Optional.of(new User()));
        when(userActionService.findAllUsersByAttributeNameAndRolePaged(any(Pageable.class), eq(role), eq(attributeName), eq(attributeValue))).thenReturn(
                ResponseEntity.ok().headers(createHeaders()).body(userDtos));

        // when
        mockMvc.perform(get("/client/actions/get/users").with(csrf())
                        .param("page", "0")
                        .param("size", "10")
                        .param("role", role)
                        .param("attributeName", attributeName)
                        .param("attributeValue", attributeValue))
        // then
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("X-Total-Count", "2"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void get_users_info_paged_should_return_users_when_role_provided() throws Exception {
        // given
        String role = "ADMIN";
        List<UserDto> userDtos = new ArrayList<>();
        when(userActionService.getLoggedUser()).thenReturn(Optional.of(new User()));
        when(userActionService.findAllUsersByRolePaged(any(Pageable.class), eq(role))).thenReturn(
                ResponseEntity.ok().headers(createHeaders()).body(userDtos));

        // when
        mockMvc.perform(get("/client/actions/get/users").with(csrf())
                        .param("page", "0")
                        .param("size", "10")
                        .param("role", role))
        // then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("X-Total-Count", "2"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void get_users_info_paged_should_return_users_when_attribute_name_provided() throws Exception {
        // given
        String attributeName = "username";
        String attributeValue = "test";
        List<UserDto> userDtos = new ArrayList<>();
        when(userActionService.getLoggedUser()).thenReturn(Optional.of(new User()));
        when(userActionService.findAllUsersByAttributeNamePaged(any(Pageable.class), eq(attributeName), eq(attributeValue))).thenReturn(
                ResponseEntity.ok().headers(createHeaders()).body(userDtos));

        // when
        mockMvc.perform(get("/client/actions/get/users").with(csrf())
                        .param("page", "0")
                        .param("size", "10")
                        .param("attributeName", attributeName)
                        .param("attributeValue", attributeValue))
        // then
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("X-Total-Count", "2"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void get_users_info_paged_should_return_users_when_no_role_or_attribute_name_provided() throws Exception {
        // given
        List<UserDto> userDtos = new ArrayList<>();
        when(userActionService.getLoggedUser()).thenReturn(Optional.of(new User()));
        when(userActionService.findAllUsersPaged(any(Pageable.class))).thenReturn(
                ResponseEntity.ok().headers(createHeaders()).body(userDtos));

        // when
        mockMvc.perform(get("/client/actions/get/users").with(csrf())
                        .param("page", "0")
                        .param("size", "10"))
        // then
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("X-Total-Count", "2"));
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Total-Count", "2");
        return headers;
    }
}