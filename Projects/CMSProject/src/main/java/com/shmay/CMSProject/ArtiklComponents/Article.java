package com.shmay.CMSProject.ArtiklComponents;

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
@Entity
@Table(name = "article")
public class Article {
    @Id
    @SequenceGenerator(
            name = "article_id_sequence",
            sequenceName = "article_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "article_id_sequence"
    )
    @JsonView({ViewAuthor.Base.class, ViewArticles.Base.class,ViewTag.Base.class, ViewCategory.Base.class})
    private Integer id;

    @Column(nullable=false)
    @JsonView({ViewAuthor.Base.class,ViewArticles.Base.class,ViewTag.Base.class,ViewCategory.Base.class})
    private String title;

    @Column(nullable=false)
    @JsonView({ViewAuthor.Base.class,ViewArticles.Base.class, ViewTag.Base.class,ViewCategory.Base.class})
    private String body;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_id",referencedColumnName = "id")
    @JsonView({ViewArticles.Base.class,ViewCategory.Base.class})
    private List<Tag> articleTags = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @JsonView({ViewArticles.Base.class,ViewTag.Base.class})
    private List<Category> articleCategory = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @JsonView({ViewArticles.Base.class,ViewCategory.Base.class,ViewTag.Base.class})
    private Author author;





    public void setTag(Tag tag){
        articleTags.add(tag);
    }
    public void setCategory(Category category){articleCategory.add(category);}
}

