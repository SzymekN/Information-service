package com.editorial.controller;

import com.editorial.model.dto.ArticleDraftDto;
import com.editorial.model.entity.User;
import com.editorial.security.SecurityConfig;
import com.editorial.service.ArticleDraftService;
import com.editorial.service.UserActionService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArticleDraftController.class)
@OverrideAutoConfiguration(enabled = true)
@ActiveProfiles("testmvc")
@Import(SecurityConfig.class)
class ArticleDraftControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleDraftService articleDraftService;

    @MockBean
    private UserActionService userActionService;

    @Test
    @WithMockUser(roles = "JOURNALIST")
    void init_article_should_return_ok() throws Exception {
        // given
        ArticleDraftDto articleDraftDto = new ArticleDraftDto();
        articleDraftDto.setTitle("Sample Title");
        articleDraftDto.setContent("Sample Content");
        User loggedUser = new User();
        loggedUser.setId(1L);
        // when
        when(userActionService.getLoggedUser()).thenReturn(Optional.of(loggedUser));
        // then
        when(articleDraftService.initArticle(any(User.class), any(ArticleDraftDto.class)))
                .thenReturn(ResponseEntity.ok("Successfully initialized an article!"));
        mockMvc.perform(post("/editorial/draft").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(articleDraftDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully initialized an article!"));
    }

    @Test
    @WithMockUser(roles = "JOURNALIST")
    void update_article_should_return_ok_when_valid_request_body() throws Exception {
        // given
        ArticleDraftDto articleDraftDto = new ArticleDraftDto();
        articleDraftDto.setId(1L);
        articleDraftDto.setTitle("Example title");
        articleDraftDto.setContent("Sample Content");
        User loggedUser = new User();
        // when
        when(userActionService.getLoggedUser()).thenReturn(Optional.of(loggedUser));
        when(articleDraftService.updateArticle(any(User.class), any(ArticleDraftDto.class)))
                .thenReturn(ResponseEntity.ok("Successful update"));
        // then
        mockMvc.perform(put("/editorial/draft").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(articleDraftDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Successful update"));
    }

    @Test
    @WithMockUser(roles = "JOURNALIST")
    void get_articles_should_return_drafts_when_logged_user_exists() throws Exception {
        // given
        Integer page = 0;
        Integer size = 10;
        User loggedUser = new User();
        // when
        when(userActionService.getLoggedUser()).thenReturn(Optional.of(loggedUser));
        when(articleDraftService.getDrafts(page, size, loggedUser))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/editorial/draft").with(csrf())
                        .param("page", page.toString())
                        .param("size", size.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "JOURNALIST")
    void delete_and_move_article_should_return_ok() throws Exception {
        // given
        Long draftId = 1L;
        User loggedUser = new User();
        // when
        when(userActionService.getLoggedUser()).thenReturn(Optional.of(loggedUser));
        when(articleDraftService.deleteAndMoveArticle(any(User.class), eq(draftId)))
                .thenReturn(ResponseEntity.ok("Successfully deleted and moved article!"));
        // then
        mockMvc.perform(delete("/editorial/draft").with(csrf())
                        .param("id", draftId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted and moved article!"));
    }

    private static String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}