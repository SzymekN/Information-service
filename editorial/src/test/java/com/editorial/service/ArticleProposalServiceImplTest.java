package com.editorial.service;

import com.editorial.model.dto.ArticleProposalDto;
import com.editorial.model.entity.ArticleProposal;
import com.editorial.model.entity.Authority;
import com.editorial.model.entity.User;
import com.editorial.repository.ArticleProposalRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("testdb")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArticleProposalServiceImplTest {
    @MockBean
    private ArticleProposalRepository articleProposalRepository;
    private ArticleProposalService articleProposalService;

    private Slice<ArticleProposal> createSampleArticleProposals() {
        List<ArticleProposal> proposals = new ArrayList<>();
        return new SliceImpl<>(proposals);
    }

    private Page<ArticleProposal> createSampleArticleProposalsPage() {
        List<ArticleProposal> proposals = new ArrayList<>();
        return new PageImpl<>(proposals);
    }

    @BeforeAll
    void setUp() {
        articleProposalService = new ArticleProposalServiceImpl(articleProposalRepository);
    }

    @Test
    void update_article_should_update_proposal() {
        // given
        ArticleProposalDto articleProposalDto = new ArticleProposalDto();
        User loggedUser = new User();
        ArticleProposal proposal = new ArticleProposal();
        proposal.setId(1L);
        Optional<ArticleProposal> optional = Optional.empty();
        // when
        when(articleProposalRepository.findById(1L)).thenReturn(optional);
        ResponseEntity<String> responseEntity = articleProposalService.updateArticle(articleProposalDto, loggedUser);
        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Proposal has not been found!", responseEntity.getBody());
    }

    @Test
    void update_article_should_save_proposal() {
        // given
        ArticleProposalDto articleProposalDto = new ArticleProposalDto();
        articleProposalDto.setId(1L);
        User loggedUser = new User();
        loggedUser.setAuthority(new Authority("JOURNALIST"));
        ArticleProposal proposal = mock(ArticleProposal.class);
        proposal.setId(1L);
        Optional<ArticleProposal> optional = Optional.of(proposal);
        // when
        when(articleProposalRepository.findById(1L)).thenReturn(optional);
        when(proposal.getJournalist()).thenReturn(loggedUser);
        ResponseEntity<String> responseEntity = articleProposalService.updateArticle(articleProposalDto, loggedUser);
        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Successful update", responseEntity.getBody());
    }
    @Test
    void update_article_should_return_bad_request_when_proposal_not_found() {
        // given
        ArticleProposalDto articleProposalDto = new ArticleProposalDto();
        User loggedUser = new User();
        // when
        when(articleProposalRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<String> responseEntity = articleProposalService.updateArticle(articleProposalDto, loggedUser);
        // then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Proposal has not been found!", responseEntity.getBody());
    }

    @Test
    void update_article_should_be_denied() {
        // given
        ArticleProposalDto articleProposalDto = new ArticleProposalDto();
        articleProposalDto.setId(1L);
        User loggedUser = new User();
        loggedUser.setAuthority(new Authority("JOURNALIST"));
        ArticleProposal proposal = mock(ArticleProposal.class);
        proposal.setId(1L);
        Optional<ArticleProposal> optional = Optional.of(proposal);
        // when
        when(articleProposalRepository.findById(1L)).thenReturn(optional);
        when(proposal.getJournalist()).thenReturn(null);
        ResponseEntity<String> responseEntity = articleProposalService.updateArticle(articleProposalDto, loggedUser);
        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void get_proposals_should_return_proposals() {
        // given
        int page = 0;
        int size = 10;
        User loggedUser = new User();
        Pageable pageable = PageRequest.of(page, size);
        loggedUser.setAuthority(new Authority("REDACTOR"));
        // when
        when(articleProposalRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(createSampleArticleProposalsPage());
        ResponseEntity<List<ArticleProposalDto>> responseEntity = articleProposalService.getProposals(pageable, loggedUser, null, null);
        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<ArticleProposalDto> proposals = responseEntity.getBody();
        assertNotNull(proposals);
    }
}