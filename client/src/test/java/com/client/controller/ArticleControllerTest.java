package com.client.controller;

import com.client.model.dto.ArticleCorrectToClientDto;
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

import static org.mockito.Mockito.doNothing;
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
                .category("politics")
                .build());
        articleDtoList.add(ArticleDto.builder()
                .title("Two")
                .category("nature")
                .build());
        articleDtoList.add(ArticleDto.builder()
                .title("Three")
                .category("politics")
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
        // given
        String category = "politics";
        List<ArticleDto> articleDtosEdited = articleDtoList;
        // when
        articleDtosEdited.remove(1);
        when(articleService.findByCategory(category)).thenReturn(articleDtosEdited);
        String expected = new ObjectMapper().writeValueAsString(articleService.findByCategory(category));
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/client/articles")
                        .param("category",category))
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

    @Test
    @WithAnonymousUser
    public void get_all_articles_paged() throws Exception {
        // given
        int page = 1;
        int size = 2;
        // when
        when(articleService.findAllPaged(page,size))
                .thenReturn(List.of(articleDtoList.get(2)));
        String expected = new ObjectMapper().writeValueAsString(new ArrayList<>(List.of(articleDtoList.get(2))));
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/client/articles/pages")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false));
    }

    @Test
    @WithAnonymousUser
    public void get_all_articles_paged_by_category() throws Exception {
        // given
        int page = 1;
        int size = 1;
        String category = "politics";
        // when
        when(articleService.findByCategoryPaged(page,size,category))
                .thenReturn(new ArrayList<>(List.of(articleDtoList.get(2))));
        String expected = new ObjectMapper().writeValueAsString(new ArrayList<>(List.of(articleDtoList.get(2))));
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/client/articles/pages")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("category", category))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false));
    }

    @Test
    @WithAnonymousUser
    public void get_all_articles_paged_400_bad_request() throws Exception {
        // given
        int page = -1;
        int size = 1;
        String category = "politics";
        // when
        when(articleService.findByCategoryPaged(page,size,category)).thenThrow(IllegalArgumentException.class);
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/client/articles/pages")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("category", category))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    void add_article_from_editorial_should_return_successful_moved_response_when_valid_request() throws Exception {
        // given
        ArticleCorrectToClientDto articleCorrectToClientDto = new ArticleCorrectToClientDto();
        articleCorrectToClientDto.setTitle("test");
        articleCorrectToClientDto.setContent("test");
        articleCorrectToClientDto.setIsCorrected(true);
        String caller = "ARTICLE_FROM_EDITORIAL";
        doNothing().when(articleService).saveArticle(articleCorrectToClientDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/client/articles/fe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Caller", caller)
                        .content(asJsonString(articleCorrectToClientDto)))
        //then
                .andExpect(status().isOk())
                .andExpect(content().string("Successful moved"));
    }

    @Test
    @WithAnonymousUser
    void add_article_from_editorial_should_return_bad_request_response_when_invalid_caller() throws Exception {
        // given
        ArticleCorrectToClientDto articleCorrectToClientDto = new ArticleCorrectToClientDto();
        articleCorrectToClientDto.setTitle("test");
        articleCorrectToClientDto.setContent("test");
        articleCorrectToClientDto.setIsCorrected(true);
        String caller = "INVALID_CALLER";

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/client/articles/fe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Caller", caller)
                        .content(asJsonString(articleCorrectToClientDto)))
        //then
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Unsuccessful transfer process in client microservice."));
    }

    private static String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
