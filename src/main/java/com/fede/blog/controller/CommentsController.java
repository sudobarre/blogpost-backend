    package com.fede.blog.controller;

    import com.fede.blog.dto.CommentsDto;
    import com.fede.blog.dto.request.PostRequest;
    import com.fede.blog.service.CommentService;
    import lombok.AllArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    import static org.springframework.http.HttpStatus.CREATED;
    import static org.springframework.http.HttpStatus.OK;

    @RestController
    @RequestMapping("/api/v1/comments")
    @AllArgsConstructor
    @Slf4j
    public class CommentsController {

        private final CommentService commentService;

        @PreAuthorize("hasAnyRole('USER','ADMIN', 'MODERATOR')")
        @PostMapping
        public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
            commentService.save(commentsDto);
            return new ResponseEntity<>(CREATED);
        }

        @GetMapping("/by-post/{postId}")
        public ResponseEntity<List<CommentsDto>> getAllCommentsFromPost(
                @PathVariable(name = "postId") Long postId,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "20") int limit,
                @RequestParam(defaultValue = "voteCount") String sortBy,
                @RequestParam(defaultValue = "DESC") String direction
        ) {
            return ResponseEntity.status(OK)
                    .body(commentService.getAllCommentsFromPost(postId, page, limit, sortBy, direction));
        }

        @GetMapping("/by-user/{userName}")
        public ResponseEntity<List<CommentsDto>> getAllCommentsFromUser(
                @PathVariable(name = "userName") String userName,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "20") int limit,
                @RequestParam(defaultValue = "createdDate", required = false) String sortBy,
                @RequestParam(defaultValue = "DESC") String direction
        ){
            return ResponseEntity.status(OK)
                    .body(commentService.getAllCommentsFromUser(userName, page, limit, sortBy, direction));
        }

        //only accessible by owner of comment
        @PreAuthorize("hasAnyRole('USER','ADMIN', 'MODERATOR')")
        @PutMapping("/edit")
        public ResponseEntity<?> editComment(@RequestBody CommentsDto commentsDto){
            return commentService.editComment(commentsDto);
        }

        //only accessible by mod, admins or owner of comment.
        @PreAuthorize("hasAnyRole('USER','ADMIN', 'MODERATOR')")
        @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable(name = "id") Long id){
        return commentService.deleteComment(id);
    }

}
