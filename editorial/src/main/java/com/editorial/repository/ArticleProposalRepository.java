package com.editorial.repository;

import com.editorial.model.entity.ArticleProposal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleProposalRepository extends JpaRepository<ArticleProposal, Long>, JpaSpecificationExecutor<ArticleProposal> {

    @Query("SELECT ap FROM ArticleProposal ap JOIN FETCH ap.journalist j")
    Slice<ArticleProposal> findAllPaged(Pageable pageable);

    @Query("SELECT ap FROM ArticleProposal ap JOIN FETCH ap.journalist j " +
            "WHERE j.id = :id")
    Slice<ArticleProposal> findAllPagedById(Pageable pageable, @Param("id") Long journalistId);
    @Override
    Page<ArticleProposal> findAll(Specification<ArticleProposal> specification, Pageable pageable);

    @Override
    long count(Specification<ArticleProposal> specification);
}
