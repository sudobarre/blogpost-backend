package com.fede.blog.controller;

import com.fede.blog.dto.request.PostRequest;
import com.fede.blog.dto.response.PostResponse;
import com.fede.blog.service.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1/post")
@AllArgsConstructor
@Slf4j
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class PostController {

    private final PostService postService;

    @PreAuthorize("hasAnyRole('USER','ADMIN', 'MODERATOR')")
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) {
        postService.create(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/all")
    public ResponseEntity<List<PostResponse>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "voteCount") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        //url would look sth like: server:port/api/v1/post/all?page=0&limit=4&sortBy=voteCount&direction=ASC
        return status(OK).body(postService.getAllPosts(page, limit,  sortBy, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable(name = "id") Long id) {
        return status(OK).body(postService.getPost(id));
    }

    @GetMapping("/by-forum/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByForum(
            @PathVariable(name = "name") String forumName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "voteCount") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        return status(OK).body(postService.getPostsByForum(forumName, page, limit,  sortBy, direction));
    }

    @GetMapping("/by-user/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(
            @PathVariable(name = "name") String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        return status(OK).body(postService.getPostsByUsername(username, page, limit,  sortBy, direction));
    }

    //only accessible by mod, admins or owner of post.
    @PreAuthorize("hasAnyRole('USER','ADMIN', 'MODERATOR')")
    @PutMapping("/edit")
    public ResponseEntity<?> editPost(@RequestBody PostRequest postRequest){
        return postService.editPost(postRequest);
    }

    //only accessible by mod, admins or owner of post.
    @PreAuthorize("hasAnyRole('USER','ADMIN', 'MODERATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable(name = "id") Long id){
        return postService.deletePost(id);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN', 'MODERATOR')")
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody @Valid PostRequest postRequest){
        postService.save(postRequest);
        return new ResponseEntity<>(OK);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN', 'MODERATOR')")
    @PostMapping("/unsave")
    public ResponseEntity<?> unsave(@RequestBody @Valid PostRequest postRequest){
        postService.unsave(postRequest);
        return new ResponseEntity<>(OK);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @GetMapping("/saved")
    public ResponseEntity<List<PostResponse>> savedPosts(){
        return new ResponseEntity<>(postService.savedPosts(), OK);
    }
}