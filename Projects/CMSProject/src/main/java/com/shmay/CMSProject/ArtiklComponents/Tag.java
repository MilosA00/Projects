package com.shmay.CMSProject.ArtiklComponents;

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
@Table(name = "tag")
public class Tag {
   @Id
   @SequenceGenerator(
           name = "tag_id_sequence",
           sequenceName = "tag_id_sequence",
           allocationSize = 1
   )
   @GeneratedValue(
           strategy = GenerationType.SEQUENCE,
           generator = "tag_id_sequence"
   )
   @JsonView({ViewArticles.Base.class, ViewTag.Base.class,ViewCategory.Base.class})
   private Integer id;


   @JsonView({ViewArticles.Base.class,ViewTag.Base.class, ViewCategory.Base.class})
   private String name;


   @ManyToMany(cascade = CascadeType.ALL)
   @JoinColumn(name = "article_id", referencedColumnName = "id")
   private List<Article> tagArticles;





   public Tag(String name) {
      this.name = name;
   }
}
