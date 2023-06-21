package com.fede.blog.controller;

import com.fede.blog.dto.response.ViewCountUpdate;
import com.fede.blog.mapper.PostMapper;
import com.fede.blog.model.Post;
import com.fede.blog.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final PostService postService;
    private final PostMapper postMapper;

    @MessageMapping("/incrementViewCount/{postId}")
    @SendTo("/topic/viewCountUpdate")
    public ViewCountUpdate incrementViewCount(@DestinationVariable Long postId) {
        int viewCount = postService.incrementViewCount(postId);
        return new ViewCountUpdate(postId, viewCount);
    }
/*
    public void notifyPostCreated(Post post) {
        // Logic to fetch and send the newly added post
        messagingTemplate.convertAndSend("/topic/postAdded", postMapper.mapToDto(post));
    }

    public void notifyPostDeleted(Long postId) {
        messagingTemplate.convertAndSend("/topic/postDeleted", postId);
    }
    */

}
