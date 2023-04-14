package com.client.controller;

import com.client.model.dto.ArticleDto;
import com.client.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/client/articles")
public class ArticleController {

    private ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<List<ArticleDto>> getAllArticles(@RequestParam(name = "category", required = false) String category) {
        List<ArticleDto> articles;

        if(category == null) articles = articleService.findAll();
        else articles = articleService.findByCategory(category);

        if(articles.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(articles);
    }

}
