package com.client.controller;

import com.client.model.dto.ArticleCorrectToClientDto;
import com.client.model.dto.ArticleDto;
import com.client.model.entity.User;
import com.client.service.ArticleService;
import com.client.service.UserActionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/client/articles")
public class ArticleController {

    private ArticleService articleService;
    private UserActionService userActionService;

    @Autowired
    public ArticleController(ArticleService articleService, UserActionService userActionService) {
        this.articleService = articleService;
        this.userActionService = userActionService;
    }

    @GetMapping
    public ResponseEntity<List<ArticleDto>> getAllArticles(@RequestParam(name = "category", required = false) String category) {
        List<ArticleDto> articles;

        if (category == null) articles = articleService.findAll();
        else articles = articleService.findByCategory(category);

        if (articles.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(articles);
    }

    @GetMapping("/pages")
    public ResponseEntity<List<ArticleDto>> getAllArticlesPaged(@RequestParam(name = "page", required = false) Integer page,
                                                                @RequestParam(name = "size", required = false) Integer size,
                                                                @RequestParam(name = "category", required = false) String category) {
        List<ArticleDto> articles;

        try {
            if (category == null)
                articles = articleService.findAllPaged(page, size);
            else
                articles = articleService.findByCategoryPaged(page, size, category);
        } catch (RuntimeException runtimeException) {
            return ResponseEntity.badRequest().build();
        }

        if (articles.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(articles);
    }

    @PostMapping("/fe")
    public ResponseEntity<String> addArticleFromEditorial(@Valid @RequestBody ArticleCorrectToClientDto articleCorrectToClientDto, @RequestHeader("X-Caller") String caller) {
        if (!"ARTICLE_FROM_EDITORIAL".equals(caller))
            return ResponseEntity.badRequest().body("Unsuccessful transfer process in client microservice.");
        else {
            articleService.saveArticle(articleCorrectToClientDto);
            return ResponseEntity.ok("Successful moved");
        }
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<String> deleteAndMoveArticleToEditorialService(@RequestParam(name = "id") Long articleId, HttpServletRequest request) {
        Optional<User> userChecker = userActionService.getLoggedUser();

        if (userChecker.isPresent()) {
            return articleService.deleteAndMoveArticleToEditorialService(articleId, request);
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username of requesting user does not exist in db!");
    }
}
