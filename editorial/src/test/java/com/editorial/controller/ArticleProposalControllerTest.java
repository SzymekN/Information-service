package com.editorial.controller;

import com.editorial.model.dto.ArticleProposalDto;
import com.editorial.model.entity.ArticleProposal;
import com.editorial.model.entity.User;
import com.editorial.security.SecurityConfig;
import com.editorial.service.ArticleProposalService;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ArticleProposalController.class)
@OverrideAutoConfiguration(enabled = true)
@ActiveProfiles("testmvc")
@Import(SecurityConfig.class)
class ArticleProposalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleProposalService articleProposalService;

    @MockBean
    private UserActionService userActionService;

    @Test
    @WithMockUser(roles = "JOURNALIST")
    void add_article_should_return_ok() throws Exception {
        // given
        ArticleProposalDto articleProposalDto = new ArticleProposalDto();
        articleProposalDto.setTitle("Sample Title");
        articleProposalDto.setAcceptance(ArticleProposal.Acceptance.PENDING);
        User loggedUser = new User();
        loggedUser.setId(1L);
        // when
        when(userActionService.getLoggedUser()).thenReturn(Optional.of(loggedUser));
        // then
        doNothing().when(articleProposalService).addArticle(any(User.class), any(ArticleProposalDto.class));
        mockMvc.perform(post("/editorial/proposal").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(articleProposalDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully added an article!"));
    }

    @Test
    @WithAnonymousUser
    void add_article_should_return_redirect_when_user_not_logged() throws Exception {
        // given
        ArticleProposalDto articleProposalDto = new ArticleProposalDto();
        articleProposalDto.setTitle("Sample Title");
        articleProposalDto.setAcceptance(ArticleProposal.Acceptance.PENDING);
        // when
        when(userActionService.getLoggedUser()).thenReturn(Optional.empty());
        // then
        mockMvc.perform(post("/editorial/proposal").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(articleProposalDto)))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "JOURNALIST")
    void update_article_should_return_ok_when_valid_request_body() throws Exception {
        // given
        ArticleProposalDto articleProposalDto = new ArticleProposalDto();
        articleProposalDto.setId(1L);
        articleProposalDto.setAcceptance(ArticleProposal.Acceptance.PENDING);
        User loggedUser = new User();
        // when
        when(userActionService.getLoggedUser()).thenReturn(Optional.of(loggedUser));
        when(articleProposalService.updateArticle(any(), any())).thenReturn(ResponseEntity.ok("Successful update"));
        // then
        mockMvc.perform(put("/editorial/proposal").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(articleProposalDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Successful update"));
    }

    @Test
    @WithMockUser(roles = "JOURNALIST")
    void get_articles_should_return_proposals_when_logged_user_exists() throws Exception {
        // given
        Integer page = 0;
        Integer size = 10;
        User loggedUser = new User();
        // when
        when(userActionService.getLoggedUser()).thenReturn(Optional.of(loggedUser));
        when(articleProposalService.getProposals(page, size, loggedUser)).thenReturn(ResponseEntity.ok(Collections.emptyList()));
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/editorial/proposal").with(csrf())
                        .param("page", page.toString())
                        .param("size", size.toString()))
                .andExpect(status().isOk());
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