package com.editorial.controller;

import com.editorial.model.dto.ArticleCorrectDto;
import com.editorial.model.entity.User;
import com.editorial.service.ArticleCorrectService;
import com.editorial.service.UserActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/editorial/correct")
public class ArticleCorrectController {

    private final UserActionService userActionService;
    private final ArticleCorrectService articleCorrectService;

    @Autowired
    public ArticleCorrectController(UserActionService userActionService, ArticleCorrectService articleCorrectService){
        this.userActionService = userActionService;
        this.articleCorrectService = articleCorrectService;
    }

    @GetMapping
    public ResponseEntity<List<ArticleCorrectDto>> getArticles(Pageable pageable, @RequestParam(value = "title", required = false) String title,
                                                               @RequestParam(value= "isCorrected", required = false) Boolean isCorrected) {
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
}
