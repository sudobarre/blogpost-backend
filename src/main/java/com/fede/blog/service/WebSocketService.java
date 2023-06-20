package com.fede.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate simpMessagingTemplate;


    @Autowired
    public WebSocketService(final SimpMessagingTemplate messagingTemplate) {
        this.simpMessagingTemplate = messagingTemplate;
    }

    public void sendMessage(final String topicSuffix){
        simpMessagingTemplate.convertAndSend("/topic/" + topicSuffix, "Default message from WS service.");
    }

}
