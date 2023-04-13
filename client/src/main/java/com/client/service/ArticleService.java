package com.client.service;

import com.client.model.dto.ArticleDto;

import java.util.List;

public interface ArticleService {
    public List<ArticleDto> findAll();
    public List<ArticleDto> findByCategory(String category);
}
