package com.fede.blog.repository;

import com.fede.blog.model.Comment;
import com.fede.blog.model.Post;
import com.fede.blog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPost(Post post, Pageable pageable);
    List<Comment> findByPost(Post post);
    //Page<Comment> findByUserContaining(User user, Pageable pageable);
    Page<Comment> findByUser(User user, Pageable pageable);
}