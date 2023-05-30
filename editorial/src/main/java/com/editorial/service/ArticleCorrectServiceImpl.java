package com.editorial.service;

import com.editorial.model.dto.ArticleCorrectDto;
import com.editorial.model.entity.ArticleCorrect;
import com.editorial.repository.ArticleCorrectRepository;
import com.editorial.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleCorrectServiceImpl implements ArticleCorrectService {

    private final ArticleCorrectRepository articleCorrectRepository;
    private final UserRepository userRepository;

    public ArticleCorrectServiceImpl(ArticleCorrectRepository articleCorrectRepository, UserRepository userRepository) {
        this.articleCorrectRepository = articleCorrectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<List<ArticleCorrectDto>> getCorrects(Pageable pageable, String title, Boolean isCorrected) {
        Slice<ArticleCorrect> articleCorrects;
        Specification<ArticleCorrect> spec = Specification.where(null);

        long totalCount;

        if (title != null)
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%"));
        if (isCorrected != null)
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isCorrected"), isCorrected));

        totalCount = articleCorrectRepository.count(spec);

        articleCorrects = articleCorrectRepository.findAll(spec, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Total-Count", Long.toString(totalCount));

        return ResponseEntity.ok().headers(headers).body(articleCorrectsToDto(articleCorrects.getContent()));
    }

    public List<ArticleCorrectDto> articleCorrectsToDto(List<ArticleCorrect> articleCorrects) {
        return articleCorrects.stream()
                .map(articleCorrect -> ArticleCorrectDto.builder()
                        .id(articleCorrect.getId())
                        .title(articleCorrect.getTitle())
                        .content(articleCorrect.getContent())
                        .dateOfCorrection(articleCorrect.getDateOfCorrection())
                        .isCorrected(articleCorrect.getIsCorrected())
                        .correctorName(articleCorrect.getCorrector() != null ? articleCorrect.getCorrector().getUsername() : "undefined")
                        .journalistName(userRepository.findUserById(articleCorrect.getJournalistId()).getUsername())
                        .category(null)
                        .build())
                .collect(Collectors.toList());
    }
}
