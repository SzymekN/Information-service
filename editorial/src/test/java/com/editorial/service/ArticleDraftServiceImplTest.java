package com.editorial.service;

import com.editorial.model.dto.ArticleDraftDto;
import com.editorial.model.entity.ArticleCorrect;
import com.editorial.model.entity.ArticleDraft;
import com.editorial.model.entity.User;
import com.editorial.repository.ArticleCorrectRepository;
import com.editorial.repository.ArticleDraftRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("testdb")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArticleDraftServiceImplTest {
    @MockBean
    private ArticleDraftRepository articleDraftRepository;

    @MockBean
    private ArticleCorrectRepository articleCorrectRepository;

    private ArticleDraftService articleDraftService;

    private Slice<ArticleDraft> createSampleArticleDrafts() {
        List<ArticleDraft> drafts = new ArrayList<>();
        return new SliceImpl<>(drafts);
    }

    @BeforeAll
    void setUp() {
        articleDraftService = new ArticleDraftServiceImpl(articleDraftRepository, articleCorrectRepository);
    }

    @Test
    void init_article_should_save_draft() {
        // given
        ArticleDraftDto articleDraftDto = new ArticleDraftDto();
        User loggedUser = new User();
        ArticleDraft articleDraft = mock(ArticleDraft.class);
        // when
        when(articleDraftRepository.save(any(ArticleDraft.class))).thenReturn(articleDraft);
        ResponseEntity<String> responseEntity = articleDraftService.initArticle(loggedUser, articleDraftDto);
        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Successfully saved", responseEntity.getBody());
        verify(articleDraftRepository, times(1)).save(any(ArticleDraft.class));
    }

    @Test
    void update_article_should_update_draft() {
        // given
        ArticleDraftDto articleDraftDto = new ArticleDraftDto();
        articleDraftDto.setId(1L);
        ArticleDraft draft = new ArticleDraft();
        draft.setId(1L);
        Optional<ArticleDraft> optional = Optional.of(draft);
        // when
        when(articleDraftRepository.findById(1L)).thenReturn(optional);
        ResponseEntity<String> responseEntity = articleDraftService.updateArticle(articleDraftDto);
        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Successful update", responseEntity.getBody());
        verify(articleDraftRepository, times(1)).save(any(ArticleDraft.class));
    }

    @Test
    void update_article_should_return_bad_request_when_draft_not_found() {
        // given
        ArticleDraftDto articleDraftDto = new ArticleDraftDto();
        // when
        when(articleDraftRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<String> responseEntity = articleDraftService.updateArticle(articleDraftDto);
        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Draft has not been found!", responseEntity.getBody());
    }

    @Test
    void get_drafts_should_return_drafts() {
        // given
        int page = 0;
        int size = 10;
        User loggedUser = new User();
        Pageable pageable = PageRequest.of(page, size);
        Slice<ArticleDraft> articleDrafts = createSampleArticleDrafts();
        // when
        when(articleDraftRepository.findAllPagedById(any(PageRequest.class), eq(loggedUser.getId()))).thenReturn(articleDrafts);
        ResponseEntity<List<ArticleDraftDto>> responseEntity = articleDraftService.getDrafts(pageable, loggedUser, null);
        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(articleDrafts.getContent().size(), responseEntity.getBody().size());
        verify(articleDraftRepository, times(1)).findAllPagedById(any(PageRequest.class), eq(loggedUser.getId()));
    }

    @Test
    void delete_and_move_article_should_move_draft_to_correct() {
        // given
        Long draftId = 1L;
        User loggedUser = new User();
        loggedUser.setId(draftId);
        ArticleDraft draftToRemove = new ArticleDraft();
        draftToRemove.setId(draftId);
        draftToRemove.setJournalist(loggedUser);
        Optional<ArticleDraft> optional = Optional.of(draftToRemove);
        ArticleCorrect articleCorrect = mock(ArticleCorrect.class);
        // when
        when(articleDraftRepository.findById(draftId)).thenReturn(optional);
        when(articleCorrectRepository.save(any(ArticleCorrect.class))).thenReturn(articleCorrect);
        ResponseEntity<String> responseEntity = articleDraftService.deleteAndMoveArticle(loggedUser, draftId);
        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Successful moved", responseEntity.getBody());
        verify(articleCorrectRepository, times(1)).save(any(ArticleCorrect.class));
        verify(articleDraftRepository, times(1)).deleteArticleDraftById(draftId);
    }

    @Test
    void delete_and_move_article_should_return_bad_request_when_draft_not_found() {
        // given
        Long draftId = 1L;
        User loggedUser = new User();
        // when
        when(articleDraftRepository.findById(draftId)).thenReturn(Optional.empty());
        ResponseEntity<String> responseEntity = articleDraftService.deleteAndMoveArticle(loggedUser, draftId);
        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Draft has not been found!", responseEntity.getBody());
    }

    @Test
    void delete_and_move_article_should_return_forbidden_when_user_not_authorized() {
        // given
        Long draftId = 1L;
        User loggedUser = new User();
        loggedUser.setId(draftId);
        User otherUser = new User();
        otherUser.setId(draftId + 1);
        ArticleDraft draftToRemove = new ArticleDraft();
        draftToRemove.setId(draftId);
        draftToRemove.setJournalist(otherUser);
        Optional<ArticleDraft> optional = Optional.of(draftToRemove);
        // when
        when(articleDraftRepository.findById(draftId)).thenReturn(optional);
        ResponseEntity<String> responseEntity = articleDraftService.deleteAndMoveArticle(loggedUser, draftId);
        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("You do not have privileges to move this article!", responseEntity.getBody());
    }
}