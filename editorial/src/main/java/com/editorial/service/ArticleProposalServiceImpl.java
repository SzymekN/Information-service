package com.editorial.service;

import com.editorial.model.dto.ArticleProposalDto;
import com.editorial.model.entity.ArticleProposal;
import com.editorial.model.entity.User;
import com.editorial.repository.ArticleProposalRepository;
import jakarta.persistence.criteria.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleProposalServiceImpl implements ArticleProposalService {

    private final ArticleProposalRepository articleProposalRepository;
    @Autowired
    public ArticleProposalServiceImpl(ArticleProposalRepository articleProposalRepository) {
        this.articleProposalRepository = articleProposalRepository;
    }

    @Override
    public ResponseEntity<String> addArticle(User loggedUser, ArticleProposalDto articleProposalDto) {
        ArticleProposal proposal = articleDtoToProposal(loggedUser, articleProposalDto);
        loggedUser.addArticleProposal(proposal);
        articleProposalRepository.save(proposal);
        return ResponseEntity.ok("Successfully added");
    }

    @Override
    public ResponseEntity<String> updateArticle(ArticleProposalDto articleProposalDto, User loggedUser) {
        Optional<ArticleProposal> proposalFromDb = articleProposalRepository.findById(articleProposalDto.getId());

        if (proposalFromDb.isPresent()) {
            ArticleProposal proposalToUpdate = proposalFromDb.get();
            if (!loggedUser.getAuthority().getAuthorityName().equals("REDACTOR") && !loggedUser.equals(proposalToUpdate.getJournalist()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have privileges to edit this article!");
            proposalToUpdate.setTitle(articleProposalDto.getTitle());
            proposalToUpdate.setAcceptance(articleProposalDto.getAcceptance());
            proposalToUpdate.setDateOfUpdate(new Timestamp(System.currentTimeMillis()));
            if (loggedUser.getAuthority().getAuthorityName().equals("JOURNALIST"))
                proposalToUpdate.setAcceptance(ArticleProposal.Acceptance.PENDING);
            articleProposalRepository.save(proposalToUpdate);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Proposal has not been found!");

        return ResponseEntity.ok("Successful update");
    }

    @Override
    public ResponseEntity<List<ArticleProposalDto>> getProposals(Pageable pageable, User loggedUser, String title, ArticleProposal.Acceptance acceptance) {
        Slice<ArticleProposal> articleProposals;
        Specification<ArticleProposal> spec = Specification.where(null);
        long totalCount;

        if (title != null)
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%"));
        if (acceptance != null)
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("acceptance"), acceptance));
        if (!loggedUser.getAuthority().getAuthorityName().equals("REDACTOR"))
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("journalist").get("id"), loggedUser.getId()));

        totalCount = articleProposalRepository.count(spec);

        articleProposals = articleProposalRepository.findAll(spec, pageable);

        HttpHeaders headers = new HttpHeaders();;
        headers.set("X-Total-Count", Long.toString(totalCount));

        return ResponseEntity.ok().headers(headers).body(articleProposalsToDto(articleProposals.getContent()));
    }

    public ArticleProposal articleDtoToProposal(User loggedUser, ArticleProposalDto articleProposalDto) {
        return ArticleProposal.builder()
                .title(articleProposalDto.getTitle())
                .acceptance(ArticleProposal.Acceptance.PENDING)
                .dateOfUpdate(new Timestamp(System.currentTimeMillis()))
                .journalist(loggedUser)
                .build();
    }

    public List<ArticleProposalDto> articleProposalsToDto(List<ArticleProposal> articleProposals) {
        return articleProposals.stream()
                .map(articleProposal -> ArticleProposalDto.builder()
                        .id(articleProposal.getId())
                        .title(articleProposal.getTitle())
                        .acceptance(articleProposal.getAcceptance())
                        .authorName(articleProposal.getJournalist().getUsername())
                        .dateOfUpdate(articleProposal.getDateOfUpdate())
                        .build())
                .collect(Collectors.toList());
    }
}
