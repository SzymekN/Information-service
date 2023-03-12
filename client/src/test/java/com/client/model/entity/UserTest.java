package com.client.model.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;
    private Endorsement endorsement;
    private Article article;

    @Test
    public void should_connect_authority_with_user() {
        //given
        user = User.builder().build();
        Authority authority = Authority.builder().build();

        //when
        user.connectAuthority(authority);

        //then
        assertEquals(authority, user.getAuthority());
    }

    @Test
    public void should_not_connect_null_authority_with_user() {
        // given
        user = User.builder().build();
        Authority authority = null;

        // when
        user.connectAuthority(authority);

        // then
        assertNull(user.getAuthority());
    }

    @Test
    public void should_connect_userDetails_with_user() {
        //given
        user = User.builder().build();
        UserDetails userDetails = UserDetails.builder().build();

        //when
        user.connectUserDetails(userDetails);

        //then
        assertEquals(userDetails, user.getUserDetails());
    }

    @Test
    public void should_not_connect_null_userDetails_with_user() {
        // given
        user = User.builder().build();
        UserDetails userDetails = null;

        // when
        user.connectUserDetails(userDetails);

        // then
        assertNull(user.getUserDetails());
    }

    @Test
    public void should_add_endorsement_to_list() {
        // given
        user = User.builder().endorsements(new ArrayList<>()).build();
        endorsement = Endorsement.builder().build();

        // when
        user.addEndorsement(endorsement);

        // then
        assertEquals(1, user.getEndorsements().size());
        assertEquals(user, endorsement.getUser());
    }

    @Test
    public void should_create_new_endorsements_list_if_null() {
        // given
        user = User.builder().build();
        endorsement = Endorsement.builder().build();

        // when
        user.addEndorsement(endorsement);

        // then
        assertEquals(1, user.getEndorsements().size());
        assertEquals(user, endorsement.getUser());
        assertEquals(endorsement, user.getEndorsements().get(0));
        assertNotNull(user.getEndorsements());
    }

    @Test
    public void should_not_add_null_endorsement_to_list() {
        // given
        user = User.builder().endorsements(new ArrayList<>()).build();
        endorsement = null;

        // when
        user.addEndorsement(endorsement);

        // then
        assertEquals(0, user.getEndorsements().size());
    }

    @Test
    public void should_remove_endorsement_from_list() {
        //given
        user = User.builder().build();
        endorsement = Endorsement.builder().build();
        user.addEndorsement(endorsement);

        //when
        user.removeEndorsement(endorsement);

        //then
        assertEquals(0, user.getEndorsements().size());
        assertNull(endorsement.getUser());
    }

    @Test
    public void should_not_remove_null_endorsement_from_list() {
        //given
        user = User.builder().build();
        endorsement = Endorsement.builder().build();
        user.addEndorsement(endorsement);

        //when
        user.removeEndorsement(null);

        //then
        assertEquals(1, user.getEndorsements().size());
        assertNotNull(endorsement.getUser());
    }

    @Test
    public void should_not_remove_endorsement_from_list_if_null() {
        //given
        user = User.builder().build();
        endorsement = Endorsement.builder().build();

        //when
        user.removeEndorsement(endorsement);

        //then
        assertNull(user.getEndorsements());
    }

    @Test
    public void should_add_article_to_list() {
        // given
        user = User.builder().articles(new ArrayList<>()).build();
        article = Article.builder().build();

        // when
        user.addArticle(article);

        // then
        assertEquals(1, user.getArticles().size());
        assertEquals(user, article.getJournalist());
    }

    @Test
    public void should_create_new_articles_list_if_null() {
        // given
        user = User.builder().build();
        article = Article.builder().build();

        // when
        user.addArticle(article);

        // then
        assertEquals(1, user.getArticles().size());
        assertEquals(user, article.getJournalist());
        assertEquals(article, user.getArticles().get(0));
        assertNotNull(user.getArticles());
    }

    @Test
    public void should_not_add_null_article_to_list() {
        // given
        user = User.builder().articles(new ArrayList<>()).build();
        article = null;

        // when
        user.addArticle(article);

        // then
        assertEquals(0, user.getArticles().size());
    }

    @Test
    public void should_remove_article_from_list() {
        //given
        user = User.builder().build();
        article = Article.builder().build();
        user.addArticle(article);

        //when
        user.removeArticle(article);

        //then
        assertEquals(0, user.getArticles().size());
        assertNull(article.getJournalist());
    }

    @Test
    public void should_not_remove_null_article_from_list() {
        //given
        user = User.builder().build();
        article = Article.builder().build();
        user.addArticle(article);

        //when
        user.removeArticle(null);

        //then
        assertEquals(1, user.getArticles().size());
        assertNotNull(article.getJournalist());
    }

    @Test
    public void should_not_remove_article_from_list_if_null() {
        //given
        user = User.builder().build();
        article = Article.builder().build();

        //when
        user.removeArticle(article);

        //then
        assertNull(user.getArticles());
    }
}
