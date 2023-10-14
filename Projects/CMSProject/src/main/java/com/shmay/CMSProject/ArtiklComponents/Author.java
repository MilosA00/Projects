package com.shmay.CMSProject.ArtiklComponents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.shmay.CMSProject.Controller.ViewAuthor;
import com.shmay.CMSProject.Controller.ViewArticles;
import com.shmay.CMSProject.Controller.ViewCategory;
import com.shmay.CMSProject.Controller.ViewTag;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity(name = "Author")
@Table(name = "author")
@JsonIgnoreProperties({ "password" })
@JsonInclude
public class Author{
    @Id
    @SequenceGenerator(
            name = "author_id_sequence",
            sequenceName = "author_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "author_id_sequence"
    )
    @JsonView({ViewAuthor.Base.class,ViewArticles.Base.class, ViewCategory.Base.class, ViewTag.Base.class})
    private Integer id;

    @JsonView({ViewAuthor.Base.class,ViewArticles.Base.class,ViewCategory.Base.class,ViewTag.Base.class})
    @Column(name = "firstName",nullable = false)
    private String firstName;

    @JsonView({ViewAuthor.Base.class,ViewArticles.Base.class,ViewCategory.Base.class,ViewTag.Base.class})
    @Column(name = "lastName",nullable = false)
    private String lastName;

    @JsonView({ViewAuthor.Base.class, ViewArticles.Base.class,ViewCategory.Base.class,ViewTag.Base.class})
    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @Column(name = "password",nullable = false,unique = true)
    private String password;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id",referencedColumnName = "id")
    @JsonView(ViewAuthor.Base.class)
    private List<Article> authorArticles = new ArrayList<>();


    public void setSingleArticle(Article article){
        this.authorArticles.add(article);
    }

}
