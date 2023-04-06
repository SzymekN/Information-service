package com.client.service;

import com.client.config.TestDbConfig;
import com.client.model.dto.ArticleDto;
import com.client.model.entity.Article;
import com.client.model.entity.Authority;
import com.client.model.entity.User;
import com.client.model.entity.UserDetails;
import com.client.repository.ArticleRepository;
import com.client.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@OverrideAutoConfiguration(enabled = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("testdb")
@ContextConfiguration(classes = TestDbConfig.class)
class ArticleServiceImplTest {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleServiceImpl articleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;
    private List<Article> articles;

    @BeforeAll
    public void setUpObjects() {

        articles = new ArrayList<>();

        user = User.builder()
                .id(Long.parseLong("1"))
                .password("user")
                .username("user")
                .enabled(true)
                .authority(Authority.builder()
                        .id(Long.parseLong("1"))
                        .authorityName("USER")
                        .build())
                .userDetails(UserDetails.builder()
                        .id(Long.parseLong("1"))
                        .name("Test")
                        .surname("TestSurname")
                        .email("Test1@email.com")
                        .city("TestCity")
                        .build())
                .endorsements(new ArrayList<>())
                .build();

        articles.add(Article.builder()
                    .id(Long.parseLong("1"))
                    .title("Visit of the president")
                    .journalist(user)
                    .content("Lorem ipsum dolor sit amet, consectetur")
                    .category("Politics")
                    .dateOfSubmission(new Timestamp(System.currentTimeMillis()))
                    .endorsements(new ArrayList<>())
                    .build());

        articles.add(Article.builder()
                    .id(Long.parseLong("2"))
                    .title("Equinox")
                    .journalist(user)
                    .content("The spring equinox here in the northern hemisphere")
                    .category("Nature")
                    .dateOfSubmission(new Timestamp(System.currentTimeMillis()))
                    .endorsements(new ArrayList<>())
                    .build());

        articles.add(Article.builder()
                    .id(Long.parseLong("3"))
                    .title("Atomic bomb")
                    .journalist(user)
                    .content("America is far less safe – and the world is far less stable")
                    .category("Politics")
                    .dateOfSubmission(new Timestamp(System.currentTimeMillis()))
                    .endorsements(new ArrayList<>())
                    .build());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        articleRepository.saveAll(articles);
    }
    @Test
    public void find_all_db() {
        // given & when
        List<Article> articlesFromDb = articleRepository.findAll();
        // then
        Assertions.assertTrue(articlesFromDb.containsAll(articles));
    }

    @Test
    public void find_by_category_db() {
        // given
        List<Article> editedArticles = articles;
        editedArticles.remove(1);
        // when
        List<Article> articlesFromDb = articleRepository.findByCategory("Politics");
        // then
        Assertions.assertTrue(articlesFromDb.containsAll(editedArticles));
    }

    @Test
    public void articles_to_dto() {
        // given
        List<Article> articlesFromDb = articleRepository.findAll();
        // when
        List<ArticleDto> articlesDto = articleService.articlesToDto(articlesFromDb);
        // then
        Assertions.assertTrue(articlesDto.stream().allMatch(Objects::nonNull));
    }

    @Test
    public void find_by_category() {
        // given & when
        List<ArticleDto> articlesFromDb = articleService.findByCategory("Politics");
        List<ArticleDto> articlesToDto = articleService.articlesToDto(articles);
        articlesToDto.remove(1);
        // then
        Assertions.assertTrue(articlesFromDb.containsAll(articlesToDto));
    }

}