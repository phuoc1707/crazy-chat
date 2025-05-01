package com.chat.app.realtimeserver.service;

import com.chat.app.realtimeserver.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessageRelayService {

    private final WebClient webClient = WebClient.create("http://localhost:8080");

    public void sendToBackend(ChatMessage message, String token) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", message.getMessage());
        body.put("urls", message.getUrls()); // thêm URL nếu có
        body.put("sender", Map.of("id", message.getSender())); // hardcode hoặc dynamic tùy bạn
        body.put("conversation", Map.of("id", message.getConversationId()));

        webClient.post()
                .uri("/api/message/add")
                .header(HttpHeaders.AUTHORIZATION, token)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(System.out::println);
    }
}
