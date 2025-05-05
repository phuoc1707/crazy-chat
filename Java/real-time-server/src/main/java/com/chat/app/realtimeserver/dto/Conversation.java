package com.chat.app.realtimeserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {
    String Name;
    Set<Long> members;
    Long createBy;
}
