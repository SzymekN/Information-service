package com.editorial.controller;

import com.editorial.model.dto.ArticleCorrectDto;
import com.editorial.model.entity.User;
import com.editorial.service.ArticleCorrectService;
import com.editorial.service.UserActionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/editorial/correct")
public class ArticleCorrectController {

    private final UserActionService userActionService;
    private final ArticleCorrectService articleCorrectService;

    @Autowired
    public ArticleCorrectController(UserActionService userActionService, ArticleCorrectService articleCorrectService) {
        this.userActionService = userActionService;
        this.articleCorrectService = articleCorrectService;
    }

    @GetMapping
    public ResponseEntity<List<ArticleCorrectDto>> getArticles(Pageable pageable, @RequestParam(value = "title", required = false) String title,
                                                               @RequestParam(value = "isCorrected", required = false) Boolean isCorrected) {
        Optional<User> userChecker = userActionService.getLoggedUser();

        if (userChecker.isPresent()) {
            try {
                return articleCorrectService.getCorrects(pageable, title, isCorrected);
            } catch (RuntimeException runtimeException) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(List.of());
    }

    @PutMapping
    public ResponseEntity<String> updateArticle(@RequestBody @Valid ArticleCorrectDto articleCorrectDto) {
        if (articleCorrectDto == null || articleCorrectDto.getId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provide a body, including id!");

        Optional<User> userChecker = userActionService.getLoggedUser();

        if (userChecker.isPresent()) {
            User loggedUser = userChecker.get();
            return articleCorrectService.updateArticle(articleCorrectDto, loggedUser);
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username of requesting user does not exist in db!");
    }

    @DeleteMapping("/reject")
    public ResponseEntity<String> deleteAndMoveArticleToArticleDraft(@RequestParam(name = "id") Long correctId) {
        Optional<User> userChecker = userActionService.getLoggedUser();

        if (userChecker.isPresent()) {
            return articleCorrectService.deleteAndMoveArticleToArticleDraft(correctId);
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username of requesting user does not exist in db!");
    }

    @DeleteMapping("/accept")
    public ResponseEntity<String> deleteAndMoveArticleToClientService(@RequestParam(name = "id") Long correctId, @RequestParam(name = "category") String category, HttpServletRequest request) {
        Optional<User> userChecker = userActionService.getLoggedUser();

        if (userChecker.isPresent()) {
            return articleCorrectService.deleteAndMoveArticleToClientService(correctId, request, category);
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username of requesting user does not exist in db!");
    }
}
