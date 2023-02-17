package com.fede.blog.repository;

import com.fede.blog.model.Forum;
import com.fede.blog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForumRepository extends JpaRepository<Forum, Long> {
    Page<Forum> findAll(Pageable pageable);
    Optional<Forum> findByName(String name);

    Page<Forum> findAllByUser( User user, Pageable pageable);

}