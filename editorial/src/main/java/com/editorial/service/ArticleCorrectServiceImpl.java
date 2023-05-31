package com.editorial.service;

import com.editorial.model.dto.ArticleCorrectDto;
import com.editorial.model.entity.ArticleCorrect;
import com.editorial.model.entity.ArticleDraft;
import com.editorial.model.entity.User;
import com.editorial.repository.ArticleCorrectRepository;
import com.editorial.repository.ArticleDraftRepository;
import com.editorial.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleCorrectServiceImpl implements ArticleCorrectService {

    private final ArticleCorrectRepository articleCorrectRepository;
    private final UserRepository userRepository;
    private final ArticleDraftRepository articleDraftRepository;

    public ArticleCorrectServiceImpl(ArticleCorrectRepository articleCorrectRepository, UserRepository userRepository, ArticleDraftRepository articleDraftRepository) {
        this.articleCorrectRepository = articleCorrectRepository;
        this.userRepository = userRepository;
        this.articleDraftRepository = articleDraftRepository;
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
                        .category(null)
                        .build())
                .collect(Collectors.toList());
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
