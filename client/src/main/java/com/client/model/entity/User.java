package com.client.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    @NotBlank(message = "Username must not be blank!")
    @Size(min = 3, max = 45, message = "Username must contain more than 2 and less than 46 characters!")
    private String username;

    @Column(name = "password")
    @NotBlank(message = "Password must not be blank!")
    @Size(min = 4, max = 75, message = "Password must contain more than 3 and less than 76 characters!")
    @Pattern(regexp = "^[A-Za-z0-9#%@!&$./]+$", message = "Password must not contain whitespace characters! It can contain characters such as: A-Z, a-z, 0-9, #%@!&$/.")
    private String password;

    @Column(name = "enabled")
    @NotNull
    private Boolean enabled;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Authority authority;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserDetails userDetails;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endorsement> endorsements;

    @OneToMany(mappedBy = "journalist", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Article> articles;

    public void connectAuthority(Authority authority) {
        if(authority == null)
            return;
        this.authority = authority;
    }

    public void connectUserDetails(UserDetails userDetails) {
        if(userDetails == null)
            return;
        this.userDetails = userDetails;
    }

    public void addEndorsement(Endorsement endorsement) {
        if (endorsement == null)
            return;
        if (endorsements == null)
            endorsements = new ArrayList<>();
        endorsements.add(endorsement);
        endorsement.setUser(this);
    }

    public void removeEndorsement(Endorsement endorsement) {
        if (endorsement == null || endorsements == null)
            return;
        endorsements.remove(endorsement);
        endorsement.setUser(null);
    }

    public void addArticle(Article article) {
        if(article == null)
            return;
        if (articles == null)
            articles = new ArrayList<>();
        articles.add(article);
        article.setJournalist(this);
    }

    public void removeArticle(Article article) {
        if(article == null || articles == null)
            return;
        articles.remove(article);
        article.setJournalist(null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        User other = (User) o;
        return Objects.equals(username, other.username);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}
