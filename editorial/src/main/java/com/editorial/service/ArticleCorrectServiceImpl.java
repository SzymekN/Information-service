package com.editorial.service;

import com.editorial.model.dto.ArticleCorrectDto;
import com.editorial.model.dto.ArticleCorrectToClientDto;
import com.editorial.model.entity.ArticleCorrect;
import com.editorial.model.entity.ArticleDraft;
import com.editorial.model.entity.User;
import com.editorial.repository.ArticleCorrectRepository;
import com.editorial.repository.ArticleDraftRepository;
import com.editorial.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.editorial.util.UrlConstants.CLIENT_ARTICLE_URL;

@Service
public class ArticleCorrectServiceImpl implements ArticleCorrectService {

    private final ArticleCorrectRepository articleCorrectRepository;
    private final UserRepository userRepository;
    private final ArticleDraftRepository articleDraftRepository;
    private final BasicServiceImpl basicService;

    public ArticleCorrectServiceImpl(ArticleCorrectRepository articleCorrectRepository, UserRepository userRepository, ArticleDraftRepository articleDraftRepository, BasicServiceImpl basicService) {
        this.articleCorrectRepository = articleCorrectRepository;
        this.userRepository = userRepository;
        this.articleDraftRepository = articleDraftRepository;
        this.basicService = basicService;
    }

    @Override
    public ResponseEntity<List<ArticleCorrectDto>> getCorrects(Pageable pageable, String title, Boolean isCorrected) {
        Slice<ArticleCorrect> articleCorrects;
        Specification<ArticleCorrect> spec = Specification.where(null);

        long totalCount;

        if (title != null)
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%"));
        if (isCorrected != null)
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isCorrected"), isCorrected));

        totalCount = articleCorrectRepository.count(spec);

        articleCorrects = articleCorrectRepository.findAll(spec, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Total-Count", Long.toString(totalCount));

        return ResponseEntity.ok().headers(headers).body(articleCorrectsToDto(articleCorrects.getContent()));
    }

    @Override
    public ResponseEntity<String> updateArticle(ArticleCorrectDto articleCorrectDto, User loggedUser) {
        Optional<ArticleCorrect> correctFromDb = articleCorrectRepository.findById(articleCorrectDto.getId());

        if (correctFromDb.isPresent()) {
            ArticleCorrect correctToUpdate = correctFromDb.get();
            correctToUpdate.setTitle(articleCorrectDto.getTitle());
            correctToUpdate.setContent(articleCorrectDto.getContent());
            correctToUpdate.setDateOfCorrection(new Timestamp(System.currentTimeMillis()));
            correctToUpdate.setIsCorrected(articleCorrectDto.getIsCorrected());
            correctToUpdate.setCorrector(loggedUser);
            articleCorrectRepository.save(correctToUpdate);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Correct has not been found!");

        return ResponseEntity.ok("Successful update");
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteAndMoveArticleToArticleDraft(Long correctId){
        Optional<ArticleCorrect> correctFromDb = articleCorrectRepository.findById(correctId);

        if (correctFromDb.isPresent()) {
            ArticleCorrect correctToRemove = correctFromDb.get();
            ArticleDraft articleDraft = correctToDraftConversion(correctToRemove);
            articleDraftRepository.save(articleDraft);
            articleCorrectRepository.deleteArticleCorrectById(correctToRemove.getId());
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Correct has not been found!");

        return ResponseEntity.ok("Successful moved");
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteAndMoveArticleToClientService(Long correctId, HttpServletRequest request, String category){
        Optional<ArticleCorrect> correctFromDb = articleCorrectRepository.findById(correctId);
        RestTemplate restTemplate = new RestTemplate();

        if (correctFromDb.isPresent()) {
            ArticleCorrect correctToRemove = correctFromDb.get();
            articleCorrectRepository.deleteArticleCorrectById(correctToRemove.getId());

            HttpHeaders headers = basicService.copyHeadersFromRequest(request);
            headers.set("X-Caller", "ARTICLE_FROM_EDITORIAL");
            ArticleCorrectToClientDto articleCorrectToClientDto = articleCorrectToDto(correctToRemove, category);
            return restTemplate.exchange(CLIENT_ARTICLE_URL, HttpMethod.POST, new HttpEntity<>(articleCorrectToClientDto, headers), String.class);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Correct has not been found!");
    }

    public List<ArticleCorrectDto> articleCorrectsToDto(List<ArticleCorrect> articleCorrects) {
        return articleCorrects.stream()
                .map(articleCorrect -> ArticleCorrectDto.builder()
                        .id(articleCorrect.getId())
                        .title(articleCorrect.getTitle())
                        .content(articleCorrect.getContent())
                        .dateOfCorrection(articleCorrect.getDateOfCorrection())
                        .isCorrected(articleCorrect.getIsCorrected())
                        .correctorName(articleCorrect.getCorrector() != null ? articleCorrect.getCorrector().getUsername() : "undefined")
                        .journalistName(userRepository.findUserById(articleCorrect.getJournalistId()).getUsername())
                        .build())
                .collect(Collectors.toList());
    }

    public ArticleCorrectToClientDto articleCorrectToDto(ArticleCorrect articleCorrect, String category) {
        return ArticleCorrectToClientDto.builder()
                        .id(articleCorrect.getId())
                        .title(articleCorrect.getTitle())
                        .content(articleCorrect.getContent())
                        .isCorrected(articleCorrect.getIsCorrected())
                        .journalistId(articleCorrect.getJournalistId())
                        .category(category)
                        .build();
    }

    public ArticleDraft correctToDraftConversion(ArticleCorrect articleCorrect){
        return ArticleDraft.builder()
                .title(articleCorrect.getTitle())
                .content(articleCorrect.getContent())
                .dateOfUpdate(articleCorrect.getDateOfCorrection() != null ? articleCorrect.getDateOfCorrection() : new Timestamp(System.currentTimeMillis()))
                .journalist(userRepository.findUserById(articleCorrect.getJournalistId()))
                .build();
    }
}
