package com.fede.blog.controller;

import com.fede.blog.dto.ForumDto;
import com.fede.blog.service.ForumService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/forum")
@AllArgsConstructor
@Slf4j
public class ForumController {

    private final ForumService forumService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createForum(@Valid @RequestBody ForumDto forumDto) {
        return forumService.save(forumDto);
    }


    @GetMapping(value = "/all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    //forum/all?page=1&limit=5&sort=published,desc&sort=title,asc
    public ResponseEntity<List<ForumDto>> getAllForums(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "followerCount") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        List<ForumDto> res = forumService.getAll(page, limit, sortBy, direction);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    @GetMapping(value = "/{forumName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ForumDto> getForum(@PathVariable(name = "forumName") String forumName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(forumService.getForum(forumName));
    }

    //only maps the name and description of forum.
    @PreAuthorize("hasAnyRole('USER','ADMIN', 'MODERATOR')")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editForum(@Valid @RequestBody ForumDto forumDto){
       forumService.edit(forumDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //only admins can delete a forum for now.
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteForum(@PathVariable(name = "id") Long id){
        return forumService.delete(id);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN', 'MODERATOR')")
    @GetMapping(value = "/by-user/{name}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ForumDto>> getForumsByUsername(
            @PathVariable(name = "name") String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        return status(HttpStatus.OK).body(forumService.getForumsByUsername(username, page, limit,  sortBy, direction));
    }
}