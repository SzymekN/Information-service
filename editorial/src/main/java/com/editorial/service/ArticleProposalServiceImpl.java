package com.editorial.service;

import com.editorial.model.dto.ArticleProposalDto;
import com.editorial.model.entity.ArticleProposal;
import com.editorial.model.entity.User;
import com.editorial.repository.ArticleProposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
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
    public void addArticle(User loggedUser, ArticleProposalDto articleProposalDto) {
        ArticleProposal proposal = articleDtoToProposal(loggedUser, articleProposalDto);
        loggedUser.addArticleProposal(proposal);
        articleProposalRepository.save(proposal);
    }

    @Override
    public ResponseEntity<String> updateArticle(ArticleProposalDto articleProposalDto, User loggedUser) {
        Optional<ArticleProposal> proposalFromDb = articleProposalRepository.findById(articleProposalDto.getId());

        if (proposalFromDb.isPresent()) {
            ArticleProposal proposalToUpdate = proposalFromDb.get();
            if (!loggedUser.getAuthority().getAuthorityName().equals("REDACTOR") && !loggedUser.equals(proposalToUpdate.getJournalist()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have privileges to edit this article!");
            proposalToUpdate.setTitle(articleProposalDto.getTitle());
            proposalToUpdate.setKeywords(articleProposalDto.getKeywords());
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
    public ResponseEntity<List<ArticleProposalDto>> getProposals(Integer page, Integer size, User loggedUser) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Slice<ArticleProposal> articleProposals;
        if (loggedUser.getAuthority().getAuthorityName().equals("REDACTOR"))
            articleProposals = articleProposalRepository.findAllPaged(pageRequest);
        else
            articleProposals = articleProposalRepository.findAllPagedById(pageRequest, loggedUser.getId());

        return ResponseEntity.ok(articleProposalsToDo(articleProposals.getContent()));
    }

    public ArticleProposal articleDtoToProposal(User loggedUser, ArticleProposalDto articleProposalDto) {
        return ArticleProposal.builder()
                .title(articleProposalDto.getTitle())
                .acceptance(ArticleProposal.Acceptance.PENDING)
                .keywords(articleProposalDto.getKeywords())
                .dateOfUpdate(new Timestamp(System.currentTimeMillis()))
                .journalist(loggedUser)
                .build();
    }

    public List<ArticleProposalDto> articleProposalsToDo(List<ArticleProposal> articleProposals) {
        return articleProposals.stream()
                .map(articleProposal -> ArticleProposalDto.builder()
                        .id(articleProposal.getId())
                        .title(articleProposal.getTitle())
                        .keywords(articleProposal.getKeywords())
                        .acceptance(articleProposal.getAcceptance())
                        .authorName(articleProposal.getJournalist().getUsername())
                        .build())
                .collect(Collectors.toList());
    }
}
