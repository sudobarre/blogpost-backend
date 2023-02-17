package com.fede.blog.mapper;

import com.fede.blog.dto.ForumDto;
import com.fede.blog.exceptions.BlogException;
import com.fede.blog.model.Forum;
import com.fede.blog.repository.UserRepository;
import com.fede.blog.security.services.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;

@Component
@Slf4j
@Transactional
public class ForumMapper {

    @Autowired
    private UserRepository userRepository;


    public ForumDto mapForumToDto(Forum forum){
        ForumDto dto = new ForumDto();
        dto.setId(forum.getForumId());
        dto.setDescription(forum.getDescription());
        dto.setNumberOfPosts(forum.getPosts().size());
        dto.setDescription(forum.getDescription());
        dto.setName(forum.getName());
        return dto;
    }

    public Forum mapDtoToForum(ForumDto forumDto){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        Forum forum = new Forum();
        forum.setDescription(forumDto.getDescription());
        forum.setUser(userRepository.findByUsername(userName)
                .orElseThrow(()->new BlogException("User not found:" + userName)));
        forum.setName(forumDto.getName());
        forum.setCreatedDate(Instant.now());
        forum.setPosts(new ArrayList<>());
        return forum;
    }
}