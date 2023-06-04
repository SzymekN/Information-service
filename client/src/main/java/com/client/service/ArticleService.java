package com.client.service;

import com.client.model.dto.ArticleCorrectToClientDto;
import com.client.model.dto.ArticleDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ArticleService {
    List<ArticleDto> findAll();
    List<ArticleDto> findByCategory(String category);
    List<ArticleDto> findAllPaged(int page, int size);
    List<ArticleDto> findByCategoryPaged(int page, int size, String category);
    void saveArticle(ArticleCorrectToClientDto articleCorrectToClientDto);
    ResponseEntity<String> deleteAndMoveArticleToEditorialService(Long articleId, HttpServletRequest request);
}
