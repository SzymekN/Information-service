package com.editorial.repository;

import com.editorial.model.entity.ArticleCorrect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleCorrectRepository extends JpaRepository<ArticleCorrect, Long>, JpaSpecificationExecutor<ArticleCorrect> {

    @Override
    Page<ArticleCorrect> findAll(Specification<ArticleCorrect> specification, Pageable pageable);

    @Override
    long count(Specification<ArticleCorrect> specification);

    @Modifying
    @Query("DELETE FROM ArticleCorrect ac WHERE ac.id =:id")
    void deleteArticleCorrectById(Long id);
}
