package com.editorial.model.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;
    private ArticleProposal articleProposal;
    private ArticleDraft articleDraft;
    private ArticleCorrect articleCorrect;

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
    public void should_add_articleProposal_to_list() {
        //given
        user = User.builder().articleProposals(new ArrayList<>()).build();
        articleProposal = ArticleProposal.builder().build();

        //when
        user.addArticleProposal(articleProposal);

        //then
        assertEquals(1, user.getArticleProposals().size());
        assertEquals(user, articleProposal.getJournalist());
    }

    @Test
    public void should_create_new_articleProposals_list_if_null() {
        //given
        user = User.builder().build();
        articleProposal = ArticleProposal.builder().build();

        //when
        user.addArticleProposal(articleProposal);

        //then
        assertEquals(1, user.getArticleProposals().size());
        assertEquals(user, articleProposal.getJournalist());
        assertEquals(articleProposal, user.getArticleProposals().get(0));
        assertNotNull(user.getArticleProposals());
    }

    @Test
    public void should_not_add_null_articleProposal_to_list() {
        //given
        user = User.builder().articleProposals(new ArrayList<>()).build();
        articleProposal = null;

        //when
        user.addArticleProposal(articleProposal);

        //then
        assertEquals(0, user.getArticleProposals().size());
    }

    @Test
    public void should_remove_articleProposal_from_list() {
        //given
        user = User.builder().build();
        articleProposal = ArticleProposal.builder().build();
        user.addArticleProposal(articleProposal);

        //when
        user.removeArticleProposal(articleProposal);

        //then
        assertEquals(0, user.getArticleProposals().size());
        assertNull(articleProposal.getJournalist());
    }

    @Test
    public void should_not_remove_null_articleProposal_from_list() {
        //given
        user = User.builder().build();
        articleProposal = ArticleProposal.builder().build();
        user.addArticleProposal(articleProposal);

        //when
        user.removeArticleProposal(null);

        //then
        assertEquals(1, user.getArticleProposals().size());
        assertNotNull(articleProposal.getJournalist());
    }

    @Test
    public void should_not_remove_articleProposal_from_list_if_null() {
        //given
        user = User.builder().build();
        articleProposal = ArticleProposal.builder().build();

        //when
        user.removeArticleProposal(articleProposal);

        //then
        assertNull(user.getArticleProposals());
    }

    @Test
    public void should_add_articleDraft_to_list() {
        //given
        user = User.builder().articleDrafts(new ArrayList<>()).build();
        articleDraft = ArticleDraft.builder().build();

        //when
        user.addArticleDraft(articleDraft);

        //then
        assertEquals(1, user.getArticleDrafts().size());
        assertEquals(user, articleDraft.getJournalist());
    }

    @Test
    public void should_create_articleDrafts_list_if_null() {
        //given
        user = User.builder().build();
        articleDraft = ArticleDraft.builder().build();

        //when
        user.addArticleDraft(articleDraft);

        //then
        assertEquals(1, user.getArticleDrafts().size());
        assertEquals(user, articleDraft.getJournalist());
        assertEquals(articleDraft, user.getArticleDrafts().get(0));
        assertNotNull(user.getArticleDrafts());
    }

    @Test
    public void should_not_add_null_articleDraft_to_list() {
        //given
        user = User.builder().articleDrafts(new ArrayList<>()).build();
        articleDraft = null;

        //when
        user.addArticleDraft(articleDraft);

        //then
        assertEquals(0, user.getArticleDrafts().size());
    }

    @Test
    public void should_remove_articleDraft_from_list() {
        //given
        user = User.builder().build();
        articleDraft = ArticleDraft.builder().build();
        user.addArticleDraft(articleDraft);

        //when
        user.removeArticleDraft(articleDraft);

        //then
        assertEquals(0, user.getArticleDrafts().size());
        assertNull(articleDraft.getJournalist());
    }

    @Test
    public void should_not_remove_null_articleDraft_from_list() {
        //given
        user = User.builder().build();
        articleDraft = ArticleDraft.builder().build();
        user.addArticleDraft(articleDraft);

        //when
        user.removeArticleDraft(null);

        //then
        assertEquals(1, user.getArticleDrafts().size());
        assertNotNull(articleDraft.getJournalist());
    }

    @Test
    public void should_not_remove_articleDraft_from_list_if_null() {
        //given
        user = User.builder().build();
        articleDraft = ArticleDraft.builder().build();

        //when
        user.removeArticleDraft(articleDraft);

        //then
        assertNull(user.getArticleDrafts());
    }

    @Test
    public void should_add_articleCorrect_to_corrector_list() {
        //given
        user = User.builder().articleCorrectsCorrector(new ArrayList<>()).build();
        articleCorrect = ArticleCorrect.builder().build();

        //when
        user.addArticleCorrectCorrector(articleCorrect);

        //then
        assertEquals(1, user.getArticleCorrectsCorrector().size());
        assertEquals(user, articleCorrect.getCorrector());
    }

    @Test
    public void should_create_new_articleCorrectsCorrector_list_if_null() {
        //given
        user = User.builder().build();
        articleCorrect = ArticleCorrect.builder().build();

        //when
        user.addArticleCorrectCorrector(articleCorrect);

        //then
        assertEquals(1, user.getArticleCorrectsCorrector().size());
        assertEquals(user, articleCorrect.getCorrector());
        assertEquals(articleCorrect, user.getArticleCorrectsCorrector().get(0));
        assertNotNull(user.getArticleCorrectsCorrector());
    }

    @Test
    public void should_not_add_null_articleCorrect_to_corrector_list() {
        // given
        user = User.builder().articleCorrectsCorrector(new ArrayList<>()).build();
        articleCorrect = null;

        // when
        user.addArticleCorrectCorrector(articleCorrect);

        // then
        assertEquals(0, user.getArticleCorrectsCorrector().size());
    }

    @Test
    public void should_remove_articleCorrect_from_corrector_list() {
        //given
        user = User.builder().build();
        articleCorrect = ArticleCorrect.builder().build();
        user.addArticleCorrectCorrector(articleCorrect);

        //when
        user.removeArticleCorrectCorrector(articleCorrect);

        //then
        assertEquals(0, user.getArticleCorrectsCorrector().size());
        assertNull(articleCorrect.getCorrector());
    }

    @Test
    public void should_not_remove_null_articleCorrect_from_corrector_list() {
        //given
        user = User.builder().build();
        articleCorrect = ArticleCorrect.builder().build();
        user.addArticleCorrectCorrector(articleCorrect);

        //when
        user.addArticleCorrectCorrector(null);

        //then
        assertEquals(1, user.getArticleCorrectsCorrector().size());
        assertNotNull(articleCorrect.getCorrector());
    }

    @Test
    public void should_not_remove_articleCorrect_from_corrector_list_if_null() {
        //given
        user = User.builder().build();
        articleCorrect = ArticleCorrect.builder().build();

        //when
        user.removeArticleCorrectCorrector(articleCorrect);

        //then
        assertNull(user.getArticleCorrectsCorrector());
    }
}
