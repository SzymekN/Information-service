package com.editorial.controller;

import com.editorial.model.dto.ArticleDraftDto;
import com.editorial.model.entity.User;
import com.editorial.service.ArticleDraftService;
import com.editorial.service.UserActionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/editorial/draft")
public class ArticleDraftController {

    private final UserActionService userActionService;
    private final ArticleDraftService articleDraftService;
    @Autowired
    public ArticleDraftController(UserActionService userActionService, ArticleDraftService articleDraftService) {
        this.userActionService = userActionService;
        this.articleDraftService = articleDraftService;
    }

    @PostMapping
    public ResponseEntity<String> initArticle(@RequestBody @Valid ArticleDraftDto articleDraftDto) {
        Optional<User> userChecker = userActionService.getLoggedUser();

        if (userChecker.isPresent()) {
            User loggedUser = userChecker.get();
            return articleDraftService.initArticle(loggedUser, articleDraftDto);
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username of requesting user does not exist in db!");
    }

    @PutMapping
    public ResponseEntity<String> updateArticle(@RequestBody @Valid ArticleDraftDto articleDraftDto) {
        if (articleDraftDto == null || articleDraftDto.getId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provide a body, including id!");

        Optional<User> userChecker = userActionService.getLoggedUser();

        if (userChecker.isPresent())
            return articleDraftService.updateArticle(articleDraftDto);
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username of requesting user does not exist in db!");
    }

    @GetMapping
    public ResponseEntity<List<ArticleDraftDto>> getArticles(@RequestParam(name = "page", required = false) Integer page,
                                                             @RequestParam(name = "size", required = false) Integer size) {
        Optional<User> userChecker = userActionService.getLoggedUser();

        if (userChecker.isPresent()) {
            User loggedUser = userChecker.get();
            try {
                return articleDraftService.getDrafts(page, size, loggedUser);
            } catch (RuntimeException runtimeException) {
                return ResponseEntity.badRequest().build();
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(List.of());
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAndMoveArticle(@RequestParam(name = "id") Long draftId) {
        Optional<User> userChecker = userActionService.getLoggedUser();

        if (userChecker.isPresent()) {
            User loggedUser = userChecker.get();
            return articleDraftService.deleteAndMoveArticle(loggedUser, draftId);
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username of requesting user does not exist in db!");
    }
}
