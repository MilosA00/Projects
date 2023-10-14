package com.shmay.CMSProject.ArtiklComponents;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.shmay.CMSProject.Controller.ViewArticles;
import com.shmay.CMSProject.Controller.ViewCategory;
import com.shmay.CMSProject.Controller.ViewTag;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "category")
public class Category {
    @Id
    @SequenceGenerator(
            name = "category_id_sequence",
            sequenceName = "category_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_id_sequence"
    )
    @JsonView({ViewArticles.Base.class,ViewTag.Base.class})
    private Integer id;

    @JsonView({ViewArticles.Base.class, ViewTag.Base.class})
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    @JsonIgnore
    private List<Article> categoryArticles;


    public Category(String name) {
        this.name = name;
    }
}
