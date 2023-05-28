package com.editorial.service;

import com.editorial.model.dto.ArticleProposalDto;
import com.editorial.model.entity.ArticleProposal;
import com.editorial.model.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ArticleProposalService {
    ResponseEntity<String> addArticle(User loggedUser, ArticleProposalDto articleProposalDto);
    ResponseEntity<String> updateArticle(ArticleProposalDto articleProposalDto, User user);
    ResponseEntity<List<ArticleProposalDto>> getProposals(Pageable pageable, User loggedUser, String title, ArticleProposal.Acceptance acceptance);
}
