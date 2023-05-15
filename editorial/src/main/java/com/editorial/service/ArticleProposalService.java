package com.editorial.service;

import com.editorial.model.dto.ArticleProposalDto;
import com.editorial.model.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ArticleProposalService {
    void addArticle(User loggedUser, ArticleProposalDto articleProposalDto);
    ResponseEntity<String> updateArticle(ArticleProposalDto articleProposalDto, User user);
    ResponseEntity<List<ArticleProposalDto>> getProposals(Integer page, Integer size, User loggedUser);
}
