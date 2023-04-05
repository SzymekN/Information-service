package com.client.repository;

import com.client.model.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a from Article a LEFT JOIN FETCH a.endorsements e " +
             "JOIN FETCH a.journalist j")
    List<Article> findAll();
    @Query("SELECT a from Article a LEFT JOIN FETCH a.endorsements e " +
            "JOIN FETCH a.journalist j WHERE a.category = :category")
    List<Article> findByCategory(@Param("category") String category);
}
