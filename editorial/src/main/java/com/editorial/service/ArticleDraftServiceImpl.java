package com.editorial.service;

import com.editorial.model.dto.ArticleDraftDto;
import com.editorial.model.entity.ArticleCorrect;
import com.editorial.model.entity.ArticleDraft;
import com.editorial.model.entity.User;
import com.editorial.repository.ArticleCorrectRepository;
import com.editorial.repository.ArticleDraftRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleDraftServiceImpl implements ArticleDraftService {

    private final ArticleDraftRepository articleDraftRepository;
    private final ArticleCorrectRepository articleCorrectRepository;

    @Autowired
    public ArticleDraftServiceImpl(ArticleDraftRepository articleDraftRepository,
                                   ArticleCorrectRepository articleCorrectRepository) {
        this.articleDraftRepository = articleDraftRepository;
        this.articleCorrectRepository = articleCorrectRepository;
    }

    @Override
    public ResponseEntity<String> initArticle(User loggedUser, ArticleDraftDto articleDraftDto) {
        ArticleDraft articleDraft = articleDtoToDraft(loggedUser, articleDraftDto);
        loggedUser.addArticleDraft(articleDraft);
        articleDraftRepository.save(articleDraft);
        return ResponseEntity.ok("Successfully saved");
    }

    @Override
    public ResponseEntity<String> updateArticle(ArticleDraftDto articleDraftDto) {
        Optional<ArticleDraft> draftFromDb = articleDraftRepository.findById(articleDraftDto.getId());

        if (draftFromDb.isPresent()) {
            ArticleDraft draftToUpdate = draftFromDb.get();
            draftToUpdate.setTitle(articleDraftDto.getTitle());
            draftToUpdate.setContent(articleDraftDto.getContent());
            draftToUpdate.setDateOfUpdate(new Timestamp(System.currentTimeMillis()));
            articleDraftRepository.save(draftToUpdate);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Draft has not been found!");

        return ResponseEntity.ok("Successful update");
    }

    @Override
    public ResponseEntity<List<ArticleDraftDto>> getDrafts(Integer page, Integer size, User loggedUser) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Slice<ArticleDraft> articleDrafts = articleDraftRepository.findAllPagedById(pageRequest, loggedUser.getId());
        return ResponseEntity.ok(articleDraftsToDto(articleDrafts.getContent()));
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteAndMoveArticle(User loggedUser, Long draftId) {
        Optional<ArticleDraft> draftFromDb = articleDraftRepository.findById(draftId);

        if (draftFromDb.isPresent()) {
            ArticleDraft draftToRemove = draftFromDb.get();
            if (!draftToRemove.getJournalist().getId().equals(loggedUser.getId()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have privileges to move this article!");
            ArticleCorrect articleCorrect = draftToCorrectConversion(draftToRemove);
            articleCorrectRepository.save(articleCorrect);
            articleDraftRepository.deleteArticleDraftById(draftToRemove.getId());
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Draft has not been found!");

        return ResponseEntity.ok("Successful moved");
    }

    public ArticleDraft articleDtoToDraft(User loggedUser, ArticleDraftDto articleDraftDto) {
        return ArticleDraft.builder()
                .title(articleDraftDto.getTitle())
                .content(articleDraftDto.getContent())
                .dateOfUpdate(new Timestamp(System.currentTimeMillis()))
                .journalist(loggedUser)
                .build();
    }

    public List<ArticleDraftDto> articleDraftsToDto(List<ArticleDraft> articleDrafts) {
        return articleDrafts.stream()
                .map(articleDraft -> ArticleDraftDto.builder()
                        .id(articleDraft.getId())
                        .title(articleDraft.getTitle())
                        .content(articleDraft.getContent())
                        .dateOfUpdate(articleDraft.getDateOfUpdate())
                        .build())
                .collect(Collectors.toList());
    }

    public ArticleCorrect draftToCorrectConversion(ArticleDraft articleDraft) {
        return ArticleCorrect.builder()
                .title(articleDraft.getTitle())
                .content(articleDraft.getContent())
                .isCorrected(false)
                .isEditedByJournalist(true)
                .journalistId(articleDraft.getJournalist().getId())
                .build();
    }
}
