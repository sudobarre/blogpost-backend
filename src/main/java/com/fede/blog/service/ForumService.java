package com.fede.blog.service;

import com.fede.blog.dto.ForumDto;
import com.fede.blog.dto.response.PostResponse;
import com.fede.blog.exceptions.BlogException;
import com.fede.blog.exceptions.ForumNotFoundException;
import com.fede.blog.exceptions.PostNotFoundException;
import com.fede.blog.mapper.ForumMapper;
import com.fede.blog.model.Forum;
import com.fede.blog.model.Post;
import com.fede.blog.model.User;
import com.fede.blog.repository.ForumRepository;
import com.fede.blog.repository.UserRepository;
import com.fede.blog.security.services.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class ForumService {
    private final ForumRepository forumRepository;
    private final ForumMapper forumMapper;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<?> save(ForumDto forumDto) {
        Optional<Forum> isForumNameRepeated = forumRepository.findByName(forumDto.getName());
        if(isForumNameRepeated.isPresent()){
            return ResponseEntity.badRequest().body("Error: Forum name is already taken!");
        }
        Forum save = forumRepository.save(forumMapper.mapDtoToForum(forumDto));
        forumDto.setId(save.getForumId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional(readOnly = true)
    public List<ForumDto> getAll(int page, int size, String sortBy, String direction) {
        try {
            Sort sort = direction.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            PageRequest pagingSort = PageRequest.of(page, size, sort);
            Page<Forum> pageForums = forumRepository.findAll(pagingSort);
            if(pageForums.hasContent()){
                return pageForums.getContent()
                        .stream()
                        .map(forumMapper::mapForumToDto).collect(toList());
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            throw new ForumNotFoundException("Forum not found!");
        }
    }


    @Transactional(readOnly = true)
    public ForumDto getForum(String forumName) {
        Forum forum = forumRepository.findByName(forumName)
                .orElseThrow(() -> new BlogException("No forum found with name: " + forumName));
        return forumMapper.mapForumToDto(forum);
    }

    @Transactional
    public void edit(ForumDto forumDto) {
        Forum forum = forumRepository.findById(forumDto.getId())
                .orElseThrow(() -> new BlogException("No forum found with ID - " + forumDto.getId()));
        forum.setDescription(forumDto.getDescription());
        forum.setName(forumDto.getName());
        //These changes will be flushed to the database automatically at the end of transaction
    }

    public ResponseEntity<?> delete(Long forumId) {
        forumRepository.deleteById(forumId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public List<ForumDto> getForumsByUsername(String username, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        Pageable pagingSort = PageRequest.of(page, size, sort);
        Page<Forum> forumPage = forumRepository.findAllByUser(user, pagingSort);
        if (forumPage.hasContent()) {
            return forumPage.getContent()
                    .stream()
                    .map(forumMapper::mapForumToDto)
                    .collect(toList());
        } else {
            return new ArrayList<>();
        }
    }
}
