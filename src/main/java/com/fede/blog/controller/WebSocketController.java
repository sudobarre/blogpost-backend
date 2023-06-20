package com.fede.blog.controller;

import com.fede.blog.dto.response.ViewCountUpdate;
import com.fede.blog.service.PostService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final PostService postService;
    public WebSocketController(SimpMessagingTemplate messagingTemplate, PostService postService) {
        this.messagingTemplate = messagingTemplate;
        this.postService = postService;
    }

    @MessageMapping("/incrementViewCount/{postId}")
    @SendTo("/topic/viewCountUpdate")
    public ViewCountUpdate incrementViewCount(@DestinationVariable Long postId) {
        int viewCount = postService.incrementViewCount(postId);
        return new ViewCountUpdate(postId, viewCount);
    }

}
