package com.editorial.controller;

import com.editorial.model.dto.ArticleCorrectDto;
import com.editorial.model.entity.User;
import com.editorial.security.SecurityConfig;
import com.editorial.service.ArticleCorrectService;
import com.editorial.service.UserActionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleCorrectController.class)
@OverrideAutoConfiguration(enabled = true)
@ActiveProfiles("testmvc")
@Import(SecurityConfig.class)
public class ArticleCorrectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserActionService userActionService;

    @MockBean
    private ArticleCorrectService articleCorrectService;

    private User loggedUser;

    @BeforeEach
    void setUp() {
        loggedUser = new User();
    }

    @Test
    @WithMockUser(roles = "REDACTOR")
    public void get_articles_should_return_ok_and_article_list_when_valid_parameters_provided() throws Exception {
        //given
        when(userActionService.getLoggedUser()).thenReturn(Optional.of(loggedUser));
        List<ArticleCorrectDto> articleCorrectDtos = new ArrayList<>();
        articleCorrectDtos.add(new ArticleCorrectDto());
        when(articleCorrectService.getCorrects(any(Pageable.class), anyString(), anyBoolean())).thenReturn(ResponseEntity.ok(articleCorrectDtos));

        //when
        mockMvc.perform(get("/editorial/correct").with(csrf())
                        .param("title", "example")
                        .param("isCorrected", "true"))
                //then
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "REDACTOR")
    public void update_article_should_return_ok_when_valid_request_body() throws Exception {
        //given
        when(userActionService.getLoggedUser()).thenReturn(Optional.of(loggedUser));
        when(articleCorrectService.updateArticle(any(ArticleCorrectDto.class), eq(loggedUser))).thenReturn(ResponseEntity.ok("Successful update"));
        ArticleCorrectDto requestBody = new ArticleCorrectDto();
        requestBody.setId(1L);
        requestBody.setTitle("test");
        requestBody.setContent("test");
        requestBody.setIsCorrected(true);

        //when
        mockMvc.perform(put("/editorial/correct").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestBody)))
                //then
                .andExpect(status().isOk())
                .andExpect(content().string("Successful update"));
    }

    @Test
    @WithMockUser(roles = "REDACTOR")
    public void delete_and_move_article_to_article_draft_should_return_ok_when_valid_id_provided() throws Exception {
        //given
        when(userActionService.getLoggedUser()).thenReturn(Optional.of(loggedUser));
        when(articleCorrectService.deleteAndMoveArticleToArticleDraft(anyLong())).thenReturn(ResponseEntity.ok("Successful moved"));

        //when
        mockMvc.perform(delete("/editorial/correct/reject").with(csrf())
                        .param("id", "1"))
                //then
                .andExpect(status().isOk())
                .andExpect(content().string("Successful moved"));
    }

    @Test
    @WithMockUser(roles = "REDACTOR")
    public void delete_and_move_article_to_client_service_should_return_ok_when_valid_id_category_and_header_provided() throws Exception {
        //given
        when(userActionService.getLoggedUser()).thenReturn(Optional.of(loggedUser));
        when(articleCorrectService.deleteAndMoveArticleToClientService(anyLong(), any(HttpServletRequest.class), anyString())).thenReturn(ResponseEntity.ok("Successful moved"));

        //when
        mockMvc.perform(delete("/editorial/correct/accept").with(csrf())
                        .param("id", "1")
                        .param("category", "example")
                        .header("X-Caller", "ARTICLE_FROM_EDITORIAL"))
                //then
                .andExpect(status().isOk())
                .andExpect(content().string("Successful moved"));
    }

    private static String asJsonString(Object object) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
