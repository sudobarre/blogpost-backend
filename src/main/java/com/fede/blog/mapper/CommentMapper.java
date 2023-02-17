package com.fede.blog.mapper;

import com.fede.blog.dto.CommentsDto;
import com.fede.blog.model.Comment;
import com.fede.blog.model.Post;
import com.fede.blog.model.User;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.Instant;

@Component
public class CommentMapper {

    public Comment map(CommentsDto commentsDto, Post post, User user){
        Comment entity = new Comment();
        entity.setUser(user);
        entity.setCommentId(commentsDto.getId());
        entity.setPost(post);
        entity.setCreatedDate(Instant.now());
        entity.setText(commentsDto.getText());
        entity.setVoteCount(commentsDto.getVoteCount());
        return entity;
    }

    public CommentsDto mapToDto(Comment comment){
        CommentsDto dto = new CommentsDto();
        dto.setPostId(comment.getPost().getPostId());
        dto.setCreatedDate(getDuration(comment));
        dto.setText(comment.getText());
        dto.setUserName(comment.getUser().getUsername());
        dto.setVoteCount(comment.getVoteCount());
        dto.setId(comment.getCommentId());
        return dto;
    }

    String getDuration(Comment comment) {
        PrettyTime prettyTime = new PrettyTime();
        Instant time = comment.getCreatedDate() == null ? Instant.now() : comment.getCreatedDate();
        return prettyTime.format(Date.from(time));
    }
}
