package com.fede.blog.service;

import com.fede.blog.dto.CommentsDto;
import com.fede.blog.exceptions.BlogException;
import com.fede.blog.exceptions.PostNotFoundException;
import com.fede.blog.mapper.CommentMapper;
import com.fede.blog.model.Comment;
import com.fede.blog.model.NotificationEmail;
import com.fede.blog.model.Post;
import com.fede.blog.model.User;
import com.fede.blog.repository.CommentRepository;
import com.fede.blog.repository.PostRepository;
import com.fede.blog.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Transactional
public class CommentService {
    //change this in the future
    //TODO: change to current domains url
    private static final String POST_URL = "http://localhost:4200" + "/post/";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailService mailService;

    public void save(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());

        commentRepository.save(comment);

        sendCommentNotification(comment.getText(), post.getUser());
    }


    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail("Someone commented on your post.",
                user.getUsername(),
                user.getUsername() + " commented on your post:\n" + message));
    }

    public List<CommentsDto> getAllCommentsFromPost(Long postId, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        Pageable pagingSort = PageRequest.of(page, size, sort);
        Page<Comment> commentPage = commentRepository.findByPost(post, pagingSort);
        if(commentPage.hasContent()){
            return commentPage.getContent()
                    .stream()
                    .map(commentMapper::mapToDto)

                    .collect(toList());
        } else {
            return new ArrayList<>();
        }
    }

    public List<CommentsDto> getAllCommentsFromUser(String userName, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        Pageable pagingSort = PageRequest.of(page, size, sort);
        Page<Comment> commentPage = commentRepository.findByUser(user, pagingSort);
        if(commentPage.hasContent()){
            return commentPage.getContent()
                    .stream()
                    .map(commentMapper::mapToDto).collect(toList());
        } else {
            return new ArrayList<>();
        }
    }

    //example for testing
    public boolean containsSwearWords(String comment) {
        if (comment.contains("shit")) {
            throw new BlogException("Comments contains unacceptable language");
        }
        return false;
    }

    public ResponseEntity<?> editComment(CommentsDto commentsDto) {

        User currentUser = authService.getCurrentUser();
        if (!Objects.equals(currentUser.getUsername(), commentsDto.getUserName())) {
            return new ResponseEntity<>("Only the author can edit their comment.", HttpStatus.FORBIDDEN);
        } else {
            Post post = postRepository.findById(commentsDto.getPostId())
                    .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
            Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
            commentRepository.save(comment);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BlogException(id + "not found!"));
        User currentUser = authService.getCurrentUser();
        if (!Objects.equals(currentUser.getUsername(), comment.getUser().getUsername())) {
            if (currentUser.getRoles().size() <= 1) {
                return new ResponseEntity<>("Only the author or mods can delete a comment.", HttpStatus.FORBIDDEN);
            }
        } else {
            commentRepository.deleteById(id);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}