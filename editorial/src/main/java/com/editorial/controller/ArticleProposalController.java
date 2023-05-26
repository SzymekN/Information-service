package com.editorial.controller;

import com.editorial.model.dto.ArticleProposalDto;
import com.editorial.model.entity.User;
import com.editorial.service.ArticleProposalService;
import com.editorial.service.UserActionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/editorial/proposal")
public class ArticleProposalController {

    private final ArticleProposalService articleProposalService;
    private final UserActionService userActionService;

    @Autowired
    public ArticleProposalController(ArticleProposalService articleProposalService, UserActionService userActionService) {
        this.articleProposalService = articleProposalService;
        this.userActionService = userActionService;
    }

    @PostMapping
    public ResponseEntity<String> addArticle(@RequestBody @Valid ArticleProposalDto articleProposalDto) {
        Optional<User> userChecker = userActionService.getLoggedUser();

        if (userChecker.isPresent()) {
            User loggedUser = userChecker.get();
            return articleProposalService.addArticle(loggedUser, articleProposalDto);
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username of requesting user does not exist in db!");
    }

    @PutMapping
    public ResponseEntity<String> updateArticle(@RequestBody @Valid ArticleProposalDto articleProposalDto) {
        if (articleProposalDto == null || articleProposalDto.getId() == null
                || articleProposalDto.getAcceptance() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provide a body, including id and status!");

        Optional<User> userChecker = userActionService.getLoggedUser();

        if (userChecker.isPresent()) {
            User loggedUser = userChecker.get();
            return articleProposalService.updateArticle(articleProposalDto, loggedUser);
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username of requesting user does not exist in db!");
    }

    @GetMapping
    public ResponseEntity<List<ArticleProposalDto>> getArticles(@RequestParam(name = "page", required = false) Integer page,
                                                                @RequestParam(name = "size", required = false) Integer size) {
        Optional<User> userChecker = userActionService.getLoggedUser();

        if (userChecker.isPresent()) {
            User loggedUser = userChecker.get();
            try {
                return articleProposalService.getProposals(page, size, loggedUser);
            } catch (RuntimeException runtimeException) {
                return ResponseEntity.badRequest().build();
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(List.of());
    }
}
