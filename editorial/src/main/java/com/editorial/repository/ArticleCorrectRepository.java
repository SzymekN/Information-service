package com.editorial.repository;

import com.editorial.model.entity.ArticleCorrect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleCorrectRepository extends JpaRepository<ArticleCorrect, Long> {
}
