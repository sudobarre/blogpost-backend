package com.fede.blog.service;

import com.fede.blog.dto.request.PostRequest;
import com.fede.blog.dto.response.PostResponse;
import com.fede.blog.exceptions.AlreadySavedException;
import com.fede.blog.exceptions.ForumNotFoundException;
import com.fede.blog.exceptions.PostNotFoundException;
import com.fede.blog.mapper.PostMapper;
import com.fede.blog.model.Forum;
import com.fede.blog.model.Post;
import com.fede.blog.model.SavedPost;
import com.fede.blog.model.User;
import com.fede.blog.repository.ForumRepository;
import com.fede.blog.repository.PostRepository;
import com.fede.blog.repository.SavedPostRepository;
import com.fede.blog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final ForumRepository forumRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;
    private final SavedPostRepository savedPostRepository;
    private final SimpMessagingTemplate messagingTemplate;


    public void create(PostRequest postRequest) {
        Forum forum = forumRepository.findByName(postRequest.getForumName())
                .orElseThrow(() -> new ForumNotFoundException(postRequest.getForumName()));
        postRepository.save(postMapper.map(postRequest, forum, authService.getCurrentUser()));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pagingSort = PageRequest.of(page, size, sort);
        Page<Post> postPage = postRepository.findAll(pagingSort);
        if (postPage.hasContent()) {
            return postPage.getContent()
                    .stream()
                    .map(postMapper::mapToDto)
                    .collect(toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByForum(String forumName, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Forum forum = forumRepository.findByName(forumName)
                .orElseThrow(() -> new ForumNotFoundException(forumName));
        Pageable pagingSort = PageRequest.of(page, size, sort);
        Page<Post> postPage = postRepository.findByForum(forum, pagingSort);
        if (postPage.hasContent()) {
            return postPage.getContent()
                    .stream()
                    .map(postMapper::mapToDto)
                    .collect(toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        Pageable pagingSort = PageRequest.of(page, size, sort);
        Page<Post> postPage = postRepository.findByUser(user, pagingSort);
        if (postPage.hasContent()) {
            return postPage.getContent()
                    .stream()
                    .map(postMapper::mapToDto)
                    .collect(toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional
    public ResponseEntity<?> editPost(PostRequest postRequest) {
        User currentUser = authService.getCurrentUser();
        if (!Objects.equals(currentUser.getUsername(), postRequest.getUsername())) {
            if (currentUser.getRoles().size() <= 1) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            Post post = postRepository.findById(postRequest.getPostId())
                    .orElseThrow(() -> new PostNotFoundException(postRequest.getPostId().toString()));
            post.setDescription(postRequest.getDescription());
            post.setPostName(postRequest.getPostName());
            post.setUrl(postRequest.getUrl());
            //These changes will be flushed to the database automatically at the end of transaction
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        User currentUser = authService.getCurrentUser();
        if (!Objects.equals(currentUser.getUsername(), post.getUser().getUsername())) {
            if (currentUser.getRoles().size() <= 1) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        List<SavedPost> savedPosts = savedPostRepository.findByPost(post);
        savedPostRepository.deleteAll(savedPosts);
        postRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public void save(PostRequest postRequest) {
        //could also find by username from the request
        User currentUser = authService.getCurrentUser();
        User postAuthor = userRepository.findByUsername(postRequest.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException(postRequest.getUsername()));
        String forumName = postRequest.getForumName();
        Forum forum = forumRepository.findByName(forumName)
                .orElseThrow(() -> new ForumNotFoundException(forumName));
        Post post = postRepository.findByForumAndUserAndPostName(forum, postAuthor, postRequest.getPostName())
                .orElseThrow(() -> new PostNotFoundException(postRequest.getPostName()));
        Optional<SavedPost> savedPostOpt = savedPostRepository.findByUserAndPost(currentUser, post);
        if (savedPostOpt.isPresent()) {
            throw new AlreadySavedException("Post already saved.");
        } else {
            SavedPost savedPost = new SavedPost();
            savedPost.setUser(currentUser);
            savedPost.setPost(post);
            savedPostRepository.save(savedPost);
        }
    }



    public List<PostResponse> savedPosts() {
        User user = authService.getCurrentUser();
        //could make it so it returns a pageable instead if its too big, or limit the amt of saved posts.
        List<SavedPost> savedPosts = savedPostRepository.findByUser(user);
        return savedPosts.stream()
                .map(SavedPost::getPost)
                .map(postMapper::mapToDto).collect(toList());
    }

    public void unsave(PostRequest postRequest) {
        User currentUser = authService.getCurrentUser();
        User postAuthor = userRepository.findByUsername(postRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(postRequest.getUsername()));
        String forumName = postRequest.getForumName();
        Forum forum = forumRepository.findByName(forumName)
                .orElseThrow(() -> new ForumNotFoundException(forumName));
        Post post = postRepository.findByForumAndUserAndPostName(forum, postAuthor, postRequest.getPostName())
                .orElseThrow(() -> new PostNotFoundException(postRequest.getPostName()));
        SavedPost savedPost = savedPostRepository.findByUserAndPost(currentUser, post)
                .orElseThrow(() -> new AlreadySavedException("Post hasn't been saved by the user yet."));
        savedPostRepository.delete(savedPost);
    }

    //for real time display of view count
   @Transactional
    public int incrementViewCount(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Integer viewCount = post.getViewCount();
        if (viewCount == null || !isInteger(viewCount.toString())) {
            viewCount = 0;
        }

        post.setViewCount(viewCount + 1);
        postRepository.save(post);
        return viewCount + 1;
    }

    @Transactional(readOnly = true)
    public int getViewCount(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return post.getViewCount();
    }


    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}