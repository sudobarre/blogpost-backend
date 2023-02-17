package com.fede.blog.repository;

import com.fede.blog.model.Forum;
import com.fede.blog.model.Post;
import com.fede.blog.model.SavedPost;
import com.fede.blog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedPostRepository extends JpaRepository<SavedPost, Long> {
    Page<Forum> findAllByUser(User user, Pageable pageable);
    List<SavedPost> findByUser(User user);

    Optional<SavedPost> findByUserAndPost(User user, Post post);

    List<SavedPost> findByPost(Post post);
}
