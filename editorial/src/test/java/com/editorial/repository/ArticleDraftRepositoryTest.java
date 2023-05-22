package com.editorial.repository;

import com.editorial.model.entity.ArticleDraft;
import com.editorial.model.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@OverrideAutoConfiguration(enabled = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("testdb")
public class ArticleDraftRepositoryTest {

    @Autowired
    private ArticleDraftRepository articleDraftRepository;

    private User journalist;

    @BeforeAll
    public void setUp() {
        journalist = User.builder()
                .id(1L)
                .username("John")
                .build();
    }

    @Test
    public void test_find_all_paged_by_id() {
        // given
        Timestamp creationTime = new Timestamp(System.currentTimeMillis());
        Timestamp creationTime2 = new Timestamp(System.currentTimeMillis()+1);
        Timestamp creationTime3 = new Timestamp(System.currentTimeMillis()+2);
        ArticleDraft draft1 = createArticleDraft(1L, journalist, creationTime);
        ArticleDraft draft2 = createArticleDraft(2L, journalist, creationTime2);
        ArticleDraft draft3 = createArticleDraft(3L, journalist, creationTime3);
        // when
        articleDraftRepository.saveAll(List.of(draft1, draft2, draft3));
        List<ArticleDraft> drafts = articleDraftRepository.findAllPagedById(Pageable.unpaged(), journalist.getId()).getContent();
        // then
        assertNotNull(drafts);
        assertTrue(List.of(draft1, draft2, draft3).containsAll(drafts));
    }

    @Test
    public void test_delete_article_draft_by_id() {
        // given
        Timestamp creationTime = new Timestamp(System.currentTimeMillis());
        ArticleDraft draft = createArticleDraft(1L, journalist, creationTime);
        // when
        articleDraftRepository.save(draft);
        articleDraftRepository.deleteArticleDraftById(draft.getId());
        // then
        assertFalse(articleDraftRepository.existsById(draft.getId()));
    }

    private ArticleDraft createArticleDraft(Long id, User journalist, Timestamp dateOfUpdate) {
        return ArticleDraft.builder()
                .id(id)
                .title("Sample title")
                .content("Sample Content")
                .journalist(journalist)
                .dateOfUpdate(dateOfUpdate)
                .build();
    }
}