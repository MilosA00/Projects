package com.shmay.CMSProject.Controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.shmay.CMSProject.ArtiklComponents.Author;
import com.shmay.CMSProject.Database.ArticleRepository;
import com.shmay.CMSProject.Database.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@ResponseBody
@RequestMapping("/api/cms/author")
public class AuthorController {
    @Autowired
    private final AuthorRepository authorRepository;

    public AuthorController(AuthorRepository authorRepository) {this.authorRepository = authorRepository;}


    record newAuthorRegister(
            String firstName,
            String lastName,
            String email,
            String password
    ){}
    @PostMapping("/register")
    public Author newAuthor(@RequestBody newAuthorRegister register) {

        Author author = new Author();

        author.setFirstName(register.firstName);
        author.setLastName(register.lastName);
        author.setEmail(register.email);
        author.setPassword(register.password);

        return authorRepository.save(author);

    }


    @GetMapping()
    @JsonView(ViewAuthor.Base.class)
    public List<Author> getAuthors(){

        Author author;
        List<Author> authorList = new ArrayList<>();

        for(int i  = 1; i <= authorRepository.findAll().size(); i++){

            if(authorRepository.findById(i).isPresent()) {
                author = authorRepository.findById(i).get();

                for (int j = 1; j <= author.getAuthorArticles().size(); j++) {

                    if (j >= 3)
                        author.getAuthorArticles().remove(j - 1);
                }
                authorList.add(author);
            }
        }

        return authorList;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthor(@PathVariable Integer id){

        if(id <= 0) {
            return new ResponseEntity<>("{\n" +
                    "    \"Error\" : \"BAD_REQUEST\"\n" +
                    "    \"message\" : \"Invalid input.\"\n" +
                    "}"
                    , HttpStatus.BAD_REQUEST);
        }

        Optional<Author> author = authorRepository.findById(id);
        if(author.isEmpty()) {
            return new ResponseEntity<>("{\n" +
                    "   \"Error\" : \"NOT_FOUNT\"\n" +
                    "   \"message\" : \"User not found.\"\n" +
                    "}"
                    ,HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(author , HttpStatus.OK);
    }

}
