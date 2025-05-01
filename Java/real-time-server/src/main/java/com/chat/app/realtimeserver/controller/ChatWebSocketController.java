package com.chat.app.realtimeserver.controller;

import com.chat.app.realtimeserver.dto.ChatMessage;
import com.chat.app.realtimeserver.service.MessageRelayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {
    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Autowired
    MessageRelayService messageRelayService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {

        String jwtToken = headerAccessor.getFirstNativeHeader("Authorization");

        // Gửi tới các client đang sub topic tương ứng
        messagingTemplate.convertAndSend("/topic/conversation/" + message.getConversationId(), message);

        // Gọi backend API để lưu message
        messageRelayService.sendToBackend(message, jwtToken);
    }
}
