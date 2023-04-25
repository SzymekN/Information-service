package com.client.service;

import com.client.model.dto.ArticleDto;
import com.client.model.entity.Article;
import com.client.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<ArticleDto> findAll() {
        List<Article> articles = articleRepository.findAll();
        return articlesToDto(articles);
    }

    @Override
    public List<ArticleDto> findByCategory(String category) {
        List<Article> articles = articleRepository.findByCategory(category);
        return articlesToDto(articles);
    }

    @Override
    public List<ArticleDto> findAllPaged(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Slice<Article> pagedArticles = articleRepository.findAllPaged(pageRequest);
        return articlesToDto(pagedArticles.getContent());
    }

    @Override
    public List<ArticleDto> findByCategoryPaged(int page, int size, String category) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Slice<Article> pagedArticles = articleRepository.findByCategoryPaged(category, pageRequest);
        return articlesToDto(pagedArticles.getContent());
    }

    public List<ArticleDto> articlesToDto(List<Article> articles) {
        return articles.stream()
                .map(article -> ArticleDto.builder()
                        .id(article.getId())
                        .title(article.getTitle())
                        .content(article.getContent())
                        .category(article.getCategory())
                        .dateOfSubmission(article.getDateOfSubmission())
                        .positiveEndorsements(article.getEndorsements().stream().filter(endorsement -> endorsement.getValue()).count())
                        .negativeEndorsements(article.getEndorsements().stream().filter(endorsement -> !endorsement.getValue()).count())
                        .authorName(article.getJournalist().getUsername())
                        .build())
                .collect(Collectors.toList());
    }
}
