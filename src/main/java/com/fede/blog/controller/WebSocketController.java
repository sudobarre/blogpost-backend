package com.fede.blog.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/websocket/votes")
    public void handleVote(int voteCount) {
        System.out.println("voteCount = " + voteCount);
        messagingTemplate.convertAndSend("/topic/votes", voteCount);
    }
}
