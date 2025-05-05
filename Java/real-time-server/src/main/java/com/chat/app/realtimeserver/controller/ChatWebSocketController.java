package com.chat.app.realtimeserver.controller;

import com.chat.app.realtimeserver.dto.ChatMessage;
import com.chat.app.realtimeserver.dto.Conversation;
import com.chat.app.realtimeserver.service.ConversationRelayService;
import com.chat.app.realtimeserver.service.MessageRelayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {
    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Autowired
    MessageRelayService messageRelayService;

    @Autowired
    ConversationRelayService conversationRelayService;



    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {

        String jwtToken = headerAccessor.getFirstNativeHeader("Authorization");

        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);
        }

        // Gọi backend API để lưu message
        messageRelayService.sendToBackend(message, jwtToken);

        // Gửi tới các client đang sub topic tương ứng
        messagingTemplate.convertAndSend("/topic/conversation/" + message.getConversationId(), message);
    }

    @MessageMapping("/chat.conversation")
    public void addCoversation(@Payload Conversation conversation, SimpMessageHeaderAccessor headerAccessor) {

        System.out.println("Received Conversation:");
        System.out.println("Name: " + conversation.getName());
        System.out.println("Members: " + conversation.getMembers());

        String jwtToken = headerAccessor.getFirstNativeHeader("Authorization");

        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);
        }

        // Gọi backend API để lưu message
        conversationRelayService.sendToBackend(conversation, jwtToken);

        // Gửi tới các client đang sub topic tương ứng
        messagingTemplate.convertAndSend("/topic/conversation/add" , conversation);
    }
//
//    @PostMapping("/ws-api/conversations/event")
//    public ResponseEntity<?> sendEvent(@RequestBody Map<String, Object> payload) {
//        broadcastConversation(payload);
//        return ResponseEntity.ok("Sent!");
//    }
//
//    public void broadcastConversation(Map<String, Object> payload) {
//        messagingTemplate.convertAndSend("/topic/conversations/add", payload);
//    }
//
//    @PostMapping("/ws-api/conversations/update/event")
//    public ResponseEntity<?> sendUpdateEvent(@RequestBody Map<String, Object> payload) {
//        broadcastUpdateConversation(payload);
//        return ResponseEntity.ok("Sent!");
//    }
//
//    public void broadcastUpdateConversation(Map<String, Object> payload) {
//        messagingTemplate.convertAndSend("/topic/conversations/update", payload);
//    }
}
