package com.shmay.CMSProject.Database;


import com.shmay.CMSProject.ArtiklComponents.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Component
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    @Query(value = "SELECT * FROM Author WHERE id = ?1",nativeQuery = true)
    Author getAuthorsForArticle(Integer id);


}
