package com.editorial.service;

import com.editorial.model.dto.ArticleCorrectDto;
import com.editorial.model.entity.ArticleCorrect;
import com.editorial.model.entity.ArticleDraft;
import com.editorial.model.entity.User;
import com.editorial.repository.ArticleCorrectRepository;
import com.editorial.repository.ArticleDraftRepository;
import com.editorial.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("testdb")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArticleCorrectServiceImplTest {
    @MockBean
    private ArticleCorrectRepository articleCorrectRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ArticleDraftRepository articleDraftRepository;
    @MockBean
    private BasicServiceImpl basicService;

    private ArticleCorrectServiceImpl articleCorrectService;

    @BeforeAll
    void setUp() {
        articleCorrectService = new ArticleCorrectServiceImpl(
                articleCorrectRepository,
                userRepository,
                articleDraftRepository,
                basicService
        );
    }

    @Test
    void get_corrects_should_return_corrects_with_matching_title_and_is_corrected() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String title = "Sample Title";
        Boolean isCorrected = true;
        User user = new User();
        user.setUsername("test");
        List<ArticleCorrect> articleCorrects = new ArrayList<>();
        ArticleCorrect articleCorrect = new ArticleCorrect();
        articleCorrect.setJournalistId(123L);
        articleCorrects.add(articleCorrect);
        Page<ArticleCorrect> expectedPage = new PageImpl<>(articleCorrects);
        when(articleCorrectRepository.count(any(Specification.class))).thenReturn(1L);
        when(articleCorrectRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(userRepository.findUserById(any(Long.class))).thenReturn(user);

        // when
        ResponseEntity<List<ArticleCorrectDto>> responseEntity = articleCorrectService.getCorrects(pageable, title, isCorrected);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<ArticleCorrectDto> articleCorrectDtos = responseEntity.getBody();
        assertNotNull(articleCorrectDtos);
        assertEquals(1, articleCorrectDtos.size());
        assertEquals("test", articleCorrectDtos.get(0).getJournalistName());
    }

    @Test
    void update_article_should_update_existing_correct_and_return_successful_update() {
        // given
        ArticleCorrectDto articleCorrectDto = new ArticleCorrectDto();
        articleCorrectDto.setId(1L);
        articleCorrectDto.setTitle("Updated Title");
        articleCorrectDto.setContent("Updated Content");
        articleCorrectDto.setIsCorrected(true);

        User loggedUser = new User();

        ArticleCorrect existingCorrect = new ArticleCorrect();
        existingCorrect.setId(1L);

        Optional<ArticleCorrect> optionalCorrect = Optional.of(existingCorrect);

        when(articleCorrectRepository.findById(1L)).thenReturn(optionalCorrect);

        // when
        ResponseEntity<String> responseEntity = articleCorrectService.updateArticle(articleCorrectDto, loggedUser);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Successful update", responseEntity.getBody());
    }

    @Test
    void update_article_should_return_bad_request_when_correct_not_found() {
        // given
        ArticleCorrectDto articleCorrectDto = new ArticleCorrectDto();
        articleCorrectDto.setId(1L);

        User loggedUser = new User();

        when(articleCorrectRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        ResponseEntity<String> responseEntity = articleCorrectService.updateArticle(articleCorrectDto, loggedUser);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Correct has not been found!", responseEntity.getBody());
        verify(articleCorrectRepository, never()).save(any(ArticleCorrect.class));
    }

    @Test
    void delete_and_move_article_to_article_draft_should_delete_correct_and_save_draft() {
        // given
        Long correctId = 1L;

        ArticleCorrect existingCorrect = new ArticleCorrect();
        existingCorrect.setId(correctId);

        Optional<ArticleCorrect> optionalCorrect = Optional.of(existingCorrect);

        when(articleCorrectRepository.findById(correctId)).thenReturn(optionalCorrect);

        // when
        ResponseEntity<String> responseEntity = articleCorrectService.deleteAndMoveArticleToArticleDraft(correctId);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Successful moved", responseEntity.getBody());

        // Verify that the correct is deleted and draft is saved
        verify(articleCorrectRepository).deleteArticleCorrectById(correctId);
        verify(articleDraftRepository).save(any(ArticleDraft.class));
    }

    @Test
    void delete_and_move_article_to_article_draft_should_return_bad_request_when_correct_not_found() {
        // given
        Long correctId = 1L;

        when(articleCorrectRepository.findById(correctId)).thenReturn(Optional.empty());

        // when
        ResponseEntity<String> responseEntity = articleCorrectService.deleteAndMoveArticleToArticleDraft(correctId);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Correct has not been found!", responseEntity.getBody());
        verify(articleCorrectRepository, never()).deleteArticleCorrectById(correctId);
        verify(articleDraftRepository, never()).save(any(ArticleDraft.class));
    }

    @Test
    void delete_and_move_article_to_client_service_should_throw_resource_access_exception() {
        // given
        Long correctId = 1L;
        String category = "Sample Category";
        ArticleCorrect existingCorrect = new ArticleCorrect();
        existingCorrect.setId(correctId);
        Optional<ArticleCorrect> optionalCorrect = Optional.of(existingCorrect);
        when(articleCorrectRepository.findById(correctId)).thenReturn(optionalCorrect);
        HttpHeaders headers = new HttpHeaders();
        when(basicService.copyHeadersFromRequest(any(HttpServletRequest.class))).thenReturn(headers);

        // when & then
        assertThrows(ResourceAccessException.class, () -> articleCorrectService.deleteAndMoveArticleToClientService(correctId, new MockHttpServletRequest(), category));
    }
}
