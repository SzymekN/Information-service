package com.client.service;

import com.client.model.dto.ArticleCorrectToClientDto;
import com.client.model.dto.ArticleDto;
import com.client.model.entity.Article;
import com.client.repository.ArticleRepository;
import com.client.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.client.util.UrlConstants.EDITORIAL_ARTICLE_URL;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final BasicServiceImpl basicService;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, UserRepository userRepository, BasicServiceImpl basicService) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.basicService = basicService;
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

    @Override
    public void saveArticle(ArticleCorrectToClientDto articleCorrectToClientDto) {
        Article article = dtoToArticle(articleCorrectToClientDto);
        articleRepository.save(article);
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteAndMoveArticleToEditorialService(Long articleId, HttpServletRequest request){
        Optional<Article> articleFromDb = articleRepository.findById(articleId);
        RestTemplate restTemplate = new RestTemplate();

        if (articleFromDb.isPresent()) {
            Article articleToRemove = articleFromDb.get();
            articleRepository.deleteArticleById(articleToRemove.getId());

            HttpHeaders headers = basicService.copyHeadersFromRequest(request);
            headers.set("X-Caller", "ARTICLE_FROM_CLIENT");
            ArticleCorrectToClientDto articleCorrectToClientDto = articleToDto(articleToRemove);
            return restTemplate.exchange(EDITORIAL_ARTICLE_URL, HttpMethod.POST, new HttpEntity<>(articleCorrectToClientDto, headers), String.class);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Article has not been found!");
    }

    public Article dtoToArticle(ArticleCorrectToClientDto articleCorrectToClientDto) {
        return Article.builder()
                .title(articleCorrectToClientDto.getTitle())
                .content(articleCorrectToClientDto.getContent())
                .category(articleCorrectToClientDto.getCategory())
                .dateOfSubmission(new Timestamp(System.currentTimeMillis()))
                .journalist(userRepository.findUserById(articleCorrectToClientDto.getJournalistId()))
                .build();
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

    public ArticleCorrectToClientDto articleToDto(Article article) {
        return ArticleCorrectToClientDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .isCorrected(false)
                .journalistId(article.getJournalist().getId())
                .category(article.getCategory())
                .build();
    }
}
