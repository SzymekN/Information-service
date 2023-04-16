package com.client.repository;

import com.client.model.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.endorsements e " +
             "JOIN FETCH a.journalist j")
    List<Article> findAll();
    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.endorsements e " +
            "JOIN FETCH a.journalist j WHERE a.category = :category")
    List<Article> findByCategory(@Param("category") String category);
    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.endorsements e " +
            "JOIN FETCH a.journalist j")
    Slice<Article> findAllPaged(Pageable pageable);
    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.endorsements e " +
            "JOIN FETCH a.journalist j WHERE a.category = :category")
    Slice<Article> findByCategoryPaged(@Param("category") String category, Pageable pageable);


}
