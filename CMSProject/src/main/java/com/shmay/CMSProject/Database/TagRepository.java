package com.shmay.CMSProject.Database;

import com.shmay.CMSProject.ArtiklComponents.Article;
import com.shmay.CMSProject.ArtiklComponents.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
public interface TagRepository extends JpaRepository<Tag, Integer> {


    @Query(value = "SELECT * FROM Tag WHERE name = ?1",nativeQuery = true)
    List<Tag> getAuthorTagsRepo(String name);

    @Query(value = "SELECT * FROM Tag WHERE name = ?1",nativeQuery = true)
    Tag getSingleTag(String name);


}
