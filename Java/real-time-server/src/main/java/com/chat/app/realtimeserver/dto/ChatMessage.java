package com.chat.app.realtimeserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private  String message;
    List<String> urls;
    private Long sender;
    private Long conversationId;
}
