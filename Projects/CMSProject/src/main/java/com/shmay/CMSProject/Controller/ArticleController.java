package com.shmay.CMSProject.Controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.shmay.CMSProject.ArtiklComponents.Article;
import com.shmay.CMSProject.ArtiklComponents.Category;
import com.shmay.CMSProject.ArtiklComponents.Tag;
import com.shmay.CMSProject.Database.ArticleRepository;
import com.shmay.CMSProject.Database.AuthorRepository;
import com.shmay.CMSProject.Database.CategoryRepository;
import com.shmay.CMSProject.Database.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;




@RestController
@ResponseBody
@RequestMapping("/api/cms/article")

public class ArticleController {
    @Autowired
    private final ArticleRepository articleRepository;
    @Autowired
    private final AuthorRepository authorRepository;
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final TagRepository tagRepository;



    public ArticleController(ArticleRepository articleRepository, AuthorRepository authorRepository, CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.articleRepository = articleRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }


    @GetMapping()
    @JsonView(ViewArticles.Base.class)
    public List<Article> article() {

        return articleRepository.findAll();
    }


    record newArticleRequest(

            Integer id,
            String title,
            String body,
            List<String> tag,
            List<String> category
    ){}
    @PostMapping("/new-article")
    public ResponseEntity<?> newArticle(@RequestBody newArticleRequest request) {

        Article newArticle = new Article();
        List<Tag> tempTag = new ArrayList<>();
        List<Category> tempCategory = new ArrayList<>();

        if(!tagRepository.findAll().isEmpty()) {
            for (int j = 0; j < request.tag.size(); j++) {
                if(tagRepository.getAuthorTagsRepo(request.tag.get(j)).isEmpty()){

                    tagRepository.save(new Tag(request.tag.get(j)));
                    tempTag.add(tagRepository.getSingleTag(request.tag.get(j)));
                } else
                    tempTag.add(tagRepository.getSingleTag(request.tag.get(j)));
            }
        }

        if(tagRepository.findAll().isEmpty()) {
            for (int i = 0; i < request.tag.size(); i++) {

                tagRepository.save(new Tag(request.tag.get(i)));
                tempTag.add(tagRepository.getSingleTag(request.tag.get(i)));
            }
        }

        for(int i = 0; i < request.category.size(); i++){
            tempCategory.add(categoryRepository.getCategory(request.category.get(i)));
        }

        if(authorRepository.findById(request.id).isPresent()){
            newArticle.setArticleTags(tempTag);
            newArticle.setBody(request.body);
            newArticle.setTitle(request.title);
            newArticle.setAuthor(authorRepository.findById(request.id).get());
            newArticle.setArticleCategory(tempCategory);

            articleRepository.save(newArticle);
            return new ResponseEntity<>("OK",HttpStatus.OK);

        }else {
            return new ResponseEntity<>("{\n" +
                    "   \"Error\" : \"NOT_FOUNT\"\n" +
                    "   \"message\" : \"User not found.\"\n" +
                    "}"
                    , HttpStatus.NOT_FOUND);
        }
    }
}

