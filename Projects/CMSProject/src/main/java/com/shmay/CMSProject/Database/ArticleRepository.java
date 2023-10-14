package com.shmay.CMSProject.Database;

import com.shmay.CMSProject.ArtiklComponents.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
public interface ArticleRepository extends JpaRepository<Article, Integer> {


    @Query(value = "SELECT * FROM Article WHERE author_id = ?1",nativeQuery = true)
    List<Article> getAuthorArticles(Integer id);

}
