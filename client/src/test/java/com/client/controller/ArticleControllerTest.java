package com.client.controller;

import com.client.model.dto.ArticleDto;
import com.client.security.SecurityConfig;
import com.client.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
@AutoConfigureMockMvc
@OverrideAutoConfiguration(enabled = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({SecurityConfig.class})
@ActiveProfiles("testmvc")
public class ArticleControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ArticleService articleService;

    private List<ArticleDto> articleDtoList;

    @BeforeAll
    public void setUp() {
        articleDtoList = new ArrayList<>();
        articleDtoList.add(ArticleDto.builder()
                .title("One")
                .category("Politics")
                .build());
        articleDtoList.add(ArticleDto.builder()
                .title("Two")
                .category("Nature")
                .build());
        articleDtoList.add(ArticleDto.builder()
                .title("Three")
                .category("Politics")
                .build());
    }

    @Test
    @WithAnonymousUser
    public void get_all_articles_no_param() throws Exception {
        // given & when
        when(articleService.findAll()).thenReturn(articleDtoList);
        String expected = new ObjectMapper().writeValueAsString(articleService.findAll());
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/client/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false));
    }

    @Test
    @WithAnonymousUser
    public void get_all_articles_with_param() throws Exception {
        // given & when
        List<ArticleDto> articleDtosEdited = articleDtoList;
        articleDtosEdited.remove(1);
        when(articleService.findByCategory("Politics")).thenReturn(articleDtosEdited);
        String expected = new ObjectMapper().writeValueAsString(articleService.findByCategory("Politics"));
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/client/articles")
                        .param("category","politics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false));
    }

    @Test
    @WithAnonymousUser
    public void get_all_articles_no_result() throws Exception {
        // given & when
        when(articleService.findAll()).thenReturn(new ArrayList<>());
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/client/articles"))
                .andExpect(status().isNoContent());
    }
}
