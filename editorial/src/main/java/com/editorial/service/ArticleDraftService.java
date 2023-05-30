package com.editorial.service;

import com.editorial.model.dto.ArticleDraftDto;
import com.editorial.model.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ArticleDraftService {
    ResponseEntity<String> initArticle(User loggedUser, ArticleDraftDto articleDraftDto);
    ResponseEntity<String> updateArticle(ArticleDraftDto articleDraftDto);
    ResponseEntity<List<ArticleDraftDto>> getDrafts(Pageable pageable, User loggedUser, String title);
    ResponseEntity<String> deleteAndMoveArticle(User loggedUser, Long draftId);
}
