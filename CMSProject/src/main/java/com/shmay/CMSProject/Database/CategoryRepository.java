package com.shmay.CMSProject.Database;

import com.shmay.CMSProject.ArtiklComponents.Article;
import com.shmay.CMSProject.ArtiklComponents.Author;
import com.shmay.CMSProject.ArtiklComponents.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface CategoryRepository extends JpaRepository<Category, Integer> {


    @Query(value = "SELECT * FROM Category WHERE name = ?1",nativeQuery = true)
    Category getCategory(String name);

}
