package com.editorial.repository;

import com.editorial.model.entity.ArticleDraft;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleDraftRepository extends JpaRepository<ArticleDraft, Long> {

    @Query("SELECT ad FROM ArticleDraft ad JOIN FETCH ad.journalist j " +
            "WHERE j.id = :id ORDER BY ad.dateOfUpdate DESC")
    Slice<ArticleDraft> findAllPagedById(Pageable pageable, @Param("id") Long journalistId);

    @Modifying
    @Query("DELETE FROM ArticleDraft ad WHERE ad.id =:id")
    void deleteArticleDraftById(Long id);
}
