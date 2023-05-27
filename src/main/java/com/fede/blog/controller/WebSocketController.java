package com.fede.blog.controller;

import com.fede.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    private PostService postService;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/websocket/votes")
    public void handleVote(int voteCount) {
        System.out.println("voteCount = " + voteCount);
        messagingTemplate.convertAndSend("/topic/votes", voteCount);
    }
    @MessageMapping("/websocket/viewCount/{postId}")
    public void handleViewCount(@DestinationVariable Long postId, int viewCount) {
        System.out.println("postId = " + postId + ", viewCount = " + viewCount);
        messagingTemplate.convertAndSend("/topic/post/" + postId + "/viewCount", viewCount);
    }
}
