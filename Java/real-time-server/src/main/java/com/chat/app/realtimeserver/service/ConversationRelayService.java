package com.chat.app.realtimeserver.service;

import com.chat.app.realtimeserver.dto.ChatMessage;
import com.chat.app.realtimeserver.dto.Conversation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConversationRelayService {

    private final WebClient webClient = WebClient.create("http://localhost:8080");

    public void sendToBackend(Conversation conversation, String token) {
        Map<String, Object> body = new HashMap<>();

        body.put("name", conversation.getName());
        body.put("members",conversation.getMembers());
        body.put("createBy",Map.of("id", conversation.getCreateBy()));

        webClient.post()
                .uri("/api/conversation/add")
                .header(HttpHeaders.AUTHORIZATION,"Bearer "+ token)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(System.out::println);
    }
}
