package com.client.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ArticleTest {

    private Article article;
    private Endorsement endorsement;

    @BeforeEach
    public void setUp(){
        endorsement = Endorsement.builder().build();
    }

    @Test
    public void should_add_endorsement_to_list() {
        // given
        article = Article.builder().endorsements(new ArrayList<>()).build();

        // when
        article.addEndorsement(endorsement);

        // then
        assertEquals(1, article.getEndorsements().size());
        assertEquals(article, endorsement.getArticle());
    }

    @Test
    public void should_create_new_endorsements_list_if_null() {
        // given
        article = Article.builder().build();

        // when
        article.addEndorsement(endorsement);

        // then
        assertEquals(1, article.getEndorsements().size());
        assertEquals(article, endorsement.getArticle());
        assertEquals(endorsement, article.getEndorsements().get(0));
        assertNotNull(article.getEndorsements());
    }

    @Test
    public void should_not_add_null_endorsement_to_list() {
        // given
        article = Article.builder().endorsements(new ArrayList<>()).build();
        endorsement = null;

        // when
        article.addEndorsement(endorsement);

        // then
        assertEquals(0, article.getEndorsements().size());
    }

    @Test
    public void should_remove_endorsement_from_list() {
        //given
        article = Article.builder().build();
        article.addEndorsement(endorsement);

        //when
        article.removeEndorsement(endorsement);

        //then
        assertEquals(0, article.getEndorsements().size());
        assertNull(endorsement.getArticle());
    }

    @Test
    public void should_not_remove_null_endorsement_from_list() {
        //given
        article = Article.builder().build();
        article.addEndorsement(endorsement);

        //when
        article.removeEndorsement(null);

        //then
        assertEquals(1, article.getEndorsements().size());
        assertNotNull(endorsement.getArticle());
    }

    @Test
    public void should_not_remove_endorsement_from_list_if_null() {
        //given
        article = Article.builder().build();

        //when
        article.removeEndorsement(endorsement);

        //then
        assertNull(article.getEndorsements());
    }
}
