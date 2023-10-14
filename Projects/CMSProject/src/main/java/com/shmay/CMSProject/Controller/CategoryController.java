package com.shmay.CMSProject.Controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.shmay.CMSProject.ArtiklComponents.Article;
import com.shmay.CMSProject.ArtiklComponents.Category;
import com.shmay.CMSProject.ArtiklComponents.Tag;
import com.shmay.CMSProject.Database.ArticleRepository;
import com.shmay.CMSProject.Database.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@ResponseBody
@RequestMapping("/api/cms/category")
public class CategoryController {

    @Autowired
    private final ArticleRepository articleRepository;

    @Autowired
    private final CategoryRepository categoryRepository;


    public CategoryController(ArticleRepository articleRepository, CategoryRepository categoryRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
    }



    @GetMapping()
    public List<Category> getCategory(){
        return categoryRepository.findAll();
    }


    @GetMapping("/{id}")
    @JsonView(ViewCategory.Base.class)
    public ResponseEntity<?> singleTagArticles(@PathVariable Integer id){

        List<Article> tempArticles = new ArrayList<>();

        if(id <= 0) {
            return new ResponseEntity<>("{\n" +
                    "    \"Error\" : \"BAD_REQUEST\"\n" +
                    "    \"message\" : \"Invalid input.\"\n" +
                    "}"
                    , HttpStatus.BAD_REQUEST);
        }

        Optional<Category> category = categoryRepository.findById(id);

        if(category.isEmpty()) {
            return new ResponseEntity<>("{\n" +
                    "   \"Error\" : \"NOT_FOUNT\"\n" +
                    "   \"message\" : \"Tag not found.\"\n" +
                    "}"
                    ,HttpStatus.NOT_FOUND);
        }

        for(int i = 1; i <= articleRepository.findAll().size(); i++ ){
            for(int j = 0; j < articleRepository.findById(i).get().getArticleCategory().size(); j++){

                if(articleRepository.findById(i).get().getArticleCategory().get(j).getId().equals(id))
                    tempArticles.add(articleRepository.findById(i).get());
            }
        }

        return new ResponseEntity<>(tempArticles , HttpStatus.OK);
    }


    record newCategoryRequest(
            List<String> categoryName
    ){}
    @PostMapping("/new-category")
    public boolean createCategory(@RequestBody newCategoryRequest request){

        for (int i  = 0; i < request.categoryName.size(); i++) {

            categoryRepository.save(new Category(request.categoryName.get(i)));
        }

        return true;
    }


    record updateCategory(
            Integer articleId,
            Integer categoryId
    ){}
    @PutMapping("/update-category")
    public boolean updateArticleTag(@RequestBody updateCategory request){

        Article article = articleRepository.findById(request.articleId).get();

        article.getArticleCategory().add(categoryRepository.findById(request.categoryId).get());
        articleRepository.save(article);

        return true;
    }


    record removeCategoryRequest(
            Integer articleId,
            Integer categoryId

    ){}
    @DeleteMapping("/remove-category")
    public boolean removeTag(@RequestBody removeCategoryRequest request) {

        Article article = articleRepository.findById(request.articleId).get();


        for (int i = 0; i < article.getArticleCategory().size(); i++) {

            if (article.getArticleCategory().get(i).getId().equals(request.categoryId)) {
                article.getArticleCategory().remove(i);
                articleRepository.save(article);
                return true;
            }
        }

        return false;
    }


}
