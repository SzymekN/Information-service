package com.editorial.repository;

import com.editorial.model.entity.ArticleProposal;
import com.editorial.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@OverrideAutoConfiguration(enabled = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("testdb")
public class ArticlePropostalRepositoryTest {

    @Autowired
    private ArticleProposalRepository articleProposalRepository;

    private ArticleProposal createArticleProposal() {
        User journalist = User.builder()
                .id(1L)
                .username("journalist1")
                .build();

        return ArticleProposal.builder()
                .id(1L)
                .title("Test Article")
                .acceptance(ArticleProposal.Acceptance.PENDING)
                .dateOfUpdate(new Timestamp(System.currentTimeMillis()))
                .journalist(journalist)
                .build();
    }

    @Test
    public void findById_shouldReturnArticleProposal() {
        // given
        ArticleProposal articleProposal = createArticleProposal();
        articleProposalRepository.save(articleProposal);
        PageRequest pageRequest = PageRequest.of(0, 10);
        // when
        Slice<ArticleProposal> foundArticleProposals = articleProposalRepository.findAllPagedById(pageRequest, articleProposal.getJournalist().getId());
        // then
        assertNotNull(foundArticleProposals);
    }
}
