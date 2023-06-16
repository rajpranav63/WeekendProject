package Project.RedditAPI.controller;

import Project.RedditAPI.Dao.DaoPostData;
import Project.RedditAPI.Dao.DaoReddit;
import Project.RedditAPI.Services.RedditService;
import Project.RedditAPI.entity.PostData;
import Project.RedditAPI.entity.RedditData;
import Project.RedditAPI.entity.RedditPostRequest;
import Project.RedditAPI.entity.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static Project.RedditAPI.Services.RedditService.postArticle;

@RestController
public class ReddiController {

    @Autowired
    private DaoReddit daoReddit;

    @Autowired
    private DaoPostData daoPostData;

    private RedditService reddit = new RedditService();

    @PostMapping("/insert/{subreddit}")
    public String getInsertInDB(@PathVariable String subreddit) throws JsonProcessingException {
        String jsonString = reddit.readArticles(subreddit);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Response responseData = objectMapper.readValue(jsonString, Response.class);
//
        for (Response.Child child : responseData.getData().getChildren()) {

            String selftextValue = child.getChildData().getSelftext();
            String authorFullName = child.getChildData().getAuthorFullName();
            String title = child.getChildData().getTitle();
            Long score = child.getChildData().getScore();
            String author = child.getChildData().getAuthor();
            String domain = child.getChildData().getDomain();
            Long created = child.getChildData().getCreated();

            Instant instant = Instant.ofEpochSecond(created);
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));

            // Format LocalDateTime as desired
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = dateTime.format(formatter);


            RedditData redditData = new RedditData(UUID.randomUUID().toString(), selftextValue, authorFullName, title, score, author, domain, created, formattedDateTime);
            daoReddit.save(redditData);
        }
//        return "Post inserted Succesfully to Database";
        return jsonString;
    }


    //Task 1
    @GetMapping("/hotnews")
    public List<RedditData> searchPosts(@RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "author", required = false) String author) {
        if ((keyword == null || keyword.isEmpty()) && (author == null || author.isEmpty())) {
            // No keyword and author provided, return all posts
            return daoReddit.findAll();
        } else if (keyword != null && !keyword.isEmpty() && author != null && !author.isEmpty()) {
            // Search by both keyword and author
            return daoReddit.findBySelftextContainingAndAuthor(keyword, author);
        } else if (keyword != null && !keyword.isEmpty()) {
            // Search by keyword only
            return daoReddit.findBySelftextContaining(keyword);
        } else if (author != null && !author.isEmpty()) {
            // Search by author only
            return daoReddit.findByAuthor(author);
        } else {
            // Invalid combination of parameters, return empty list
            return Collections.emptyList();
        }

    }

    @PostMapping("/post")
    public String postOnReddit(@RequestBody RedditPostRequest request) {
        String subReddit = request.getSubReddit();
        String title = request.getTitle();
        String content = request.getContent();
//        String url = request.getUrl();
        String store = postArticle(subReddit, title, content);
//        System.out.println(store);
        PostData postData = new PostData(UUID.randomUUID().toString(), subReddit, title, content);
        daoPostData.save(postData);
        return "Post submitted and Stored in Database successfully!";
    }


    @DeleteMapping("/delete/{author}")
    public String deletePosts(@PathVariable String author) {
        daoReddit.deleteByAuthor(author);
        return "Post with Author Name :" + author + " deleted";
    }

//    @GetMapping("/sort")
//    public List<RedditData> getSortedPosts() {
//        Sort sort = Sort.by(Sort.Direction.ASC, "date"); // Replace "fieldName" with the actual field name
//        return daoReddit.findAllByOrderByDateAsc(sort);
//    }

    @GetMapping("/sort")
    public Page<RedditData> getSortedPosts(@RequestParam(value = "page", required = true) int page, @RequestParam(value = "size", required = true) int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "date"); // Replace "fieldName" with the actual field name
        Pageable pageable = PageRequest.of(page, size, sort);
        return daoReddit.findAll(pageable);
//        return daoReddit.findAllByOrderByDateAsc(sort);
    }

    @GetMapping("/customHeader")
    ResponseEntity<String> customHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Custom-Header", "foo");

        return new ResponseEntity<>(
                "Custom header set", headers, HttpStatus.OK);
    }


}

