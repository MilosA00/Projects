package com.shmay.CMSProject.Controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.shmay.CMSProject.ArtiklComponents.Article;
import com.shmay.CMSProject.ArtiklComponents.Author;
import com.shmay.CMSProject.ArtiklComponents.Tag;
import com.shmay.CMSProject.Database.ArticleRepository;
import com.shmay.CMSProject.Database.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@ResponseBody
@RequestMapping("/api/cms/tags")
public class TagController {

    @Autowired
    private final TagRepository tagRepository;
    @Autowired
    private final ArticleRepository articleRepository;

    public TagController(TagRepository tagRepository, ArticleRepository articleRepository) {
        this.tagRepository = tagRepository;
        this.articleRepository = articleRepository;
    }


    @GetMapping
    @JsonView(ViewTag.Base.class)
    public List<Tag> allTags(){
        return tagRepository.findAll();
    }


    @GetMapping("/{id}")
    @JsonView(ViewTag.Base.class)
    public ResponseEntity<?> singleTagArticles(@PathVariable Integer id){

       List<Article> tempArticles = new ArrayList<>();

        if(id <= 0) {
            return new ResponseEntity<>("{\n" +
                    "    \"Error\" : \"BAD_REQUEST\"\n" +
                    "    \"message\" : \"Invalid input.\"\n" +
                    "}"
                    , HttpStatus.BAD_REQUEST);
        }

        Optional<Tag> tag = tagRepository.findById(id);
        if(tag.isEmpty()) {
            return new ResponseEntity<>("{\n" +
                    "   \"Error\" : \"NOT_FOUNT\"\n" +
                    "   \"message\" : \"Tag not found.\"\n" +
                    "}"
                    ,HttpStatus.NOT_FOUND);
        }

        for(int i = 1; i <= articleRepository.findAll().size(); i++ ){
            for(int j = 0; j < articleRepository.findById(i).get().getArticleTags().size(); j++){

                if(articleRepository.findById(i).get().getArticleTags().get(j).getId().equals(id))
                    tempArticles.add(articleRepository.findById(i).get());
            }

        }

        return new ResponseEntity<>(tempArticles , HttpStatus.OK);
    }


    record newTagRequest(
            Integer articleId,
            String tagName
    ){}
    @PostMapping("/new-tag")
    public boolean newTag(@RequestBody newTagRequest request){

        Article article = articleRepository.findById(request.articleId).get();

        if(article.getArticleTags().contains(tagRepository.getSingleTag(request.tagName)))
            return false;
        else{
            article.setTag(new Tag(request.tagName));
            articleRepository.save(article);
            return true;
        }
    }


    record updateTag(
            Integer articleId,
            Integer tagId,
            String newTagName
    ){}
    @PutMapping("/update-tag")
    public boolean updateArticleTag(@RequestBody updateTag request){

        Article article = articleRepository.findById(request.articleId).get();

        for(int i = 0; i < article.getArticleTags().size(); i++){

            if (article.getArticleTags().get(i).getId().equals(request.tagId)) {
                article.getArticleTags().set(i, new Tag(request.newTagName));
                articleRepository.save(article);
                return true;
            }
        }
        return false;
    }


    record removeTagRequest(
            Integer articleId,
            Integer tagId,
            boolean deleteAll
    ){}
    @DeleteMapping("/remove-tag")
    public boolean removeTag(@RequestBody removeTagRequest request) {

        Article article = articleRepository.findById(request.articleId).get();

        if (request.deleteAll) {
            article.getArticleTags().clear();
            articleRepository.save(article);
            return true;
        } else {
            for (int i = 0; i < article.getArticleTags().size(); i++) {

                if (article.getArticleTags().get(i).getId().equals(request.tagId)) {
                    article.getArticleTags().remove(i);
                    articleRepository.save(article);
                    return true;
                }
            }
        }
        return false;
    }

}
