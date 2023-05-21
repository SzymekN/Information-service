package com.client.controller;

import com.client.model.dto.UserRegistrationDto;
import com.client.model.entity.Authority;
import com.client.model.entity.User;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        HttpServletRequest request = mock(HttpServletRequest.class);
        User loggedUser = new User();
        loggedUser.setId(1L);
        loggedUser.setAuthority(new Authority("ADMIN"));
        User userToEdit = new User();
        userRegistrationDto.setName("name");
        userRegistrationDto.setSurname("name");
        userRegistrationDto.setSupplier("API");
        userRegistrationDto.setPassword("test2");
        userRegistrationDto.setEmail("email@gmail.com");
        userRegistrationDto.setUsername("test");
        // when
        when(userActionService.getLoggedUser()).thenReturn(Optional.of(loggedUser));
        when(userRepository.findUserById(userId)).thenReturn(userToEdit);
        when(userActionService.updateUserClientToEditorial(any(Long.class), any(UserRegistrationDto.class),
                any(HttpServletRequest.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        // then
        mockMvc.perform(put("/client/actions/edit").with(csrf())
                        .param("id", userId.toString())
                        .content(new ObjectMapper().writeValueAsString(userRegistrationDto))
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
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        HttpServletRequest request = mock(HttpServletRequest.class);
        userRegistrationDto.setName("name");
        userRegistrationDto.setSurname("name");
        userRegistrationDto.setSupplier("API");
        userRegistrationDto.setPassword("test2");
        userRegistrationDto.setEmail("WRONGEMAIL");
        userRegistrationDto.setUsername("test");;
        // when & then
        mockMvc.perform(put("/client/actions/edit").with(csrf())
                        .param("id", userId.toString())
                        .content(new ObjectMapper().writeValueAsString(userRegistrationDto))
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
}