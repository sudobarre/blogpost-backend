package com.fede.blog.mapper;

import com.fede.blog.dto.request.PostRequest;
import com.fede.blog.dto.response.PostResponse;
import com.fede.blog.model.*;
import com.fede.blog.repository.CommentRepository;
import com.fede.blog.repository.VoteRepository;
import com.fede.blog.service.AuthService;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;

import static com.fede.blog.model.VoteType.DOWNVOTE;
import static com.fede.blog.model.VoteType.UPVOTE;

@Component
public class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;

    public Post map(PostRequest postRequest, Forum forum, User currentUser) {
    Post post = new Post();
    //postRequest doesnt require an id
    post.setPostName(postRequest.getPostName());
    post.setUrl(postRequest.getUrl());
    post.setDescription(postRequest.getDescription());
    post.setForum(forum);
    post.setVoteCount(0);
    post.setUser(currentUser);
    post.setCreatedDate(Instant.now());
    return post;
    }

    public PostResponse mapToDto(Post post) {
        PostResponse dto = new PostResponse();
        dto.setId(post.getPostId());
        dto.setTitle(post.getPostName());
        dto.setUserName(post.getUser().getUsername());
        dto.setCommentCount(commentCount(post));
        dto.setCreatedAt(getDuration(post));
        dto.setVoteCount(post.getVoteCount());
        dto.setDownVote(isPostDownVoted(post));
        dto.setUpVote(isPostUpVoted(post));
        dto.setUrl(post.getUrl());
        dto.setDescription(post.getDescription());
        dto.setForumName(post.getForum().getName());
        return dto;
    }
    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        PrettyTime prettyTime = new PrettyTime();
        return prettyTime.format(Date.from(post.getCreatedDate()));
    }

    boolean isPostUpVoted(Post post) {
        return checkVoteType(post, UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, DOWNVOTE);
    }



    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
                            authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }
}
