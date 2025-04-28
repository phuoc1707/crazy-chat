package com.chat.app.serverApi.controller;

import com.chat.app.serverApi.entity.Conversation;
import com.chat.app.serverApi.entity.User;
import com.chat.app.serverApi.exceptionHandler.ApiResponse;
import com.chat.app.serverApi.service.ConversationService;
import io.jsonwebtoken.lang.Collections;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/conversation")
@RequiredArgsConstructor
@Validated
public class ConversationController {

    @Autowired
    ConversationService conversationService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Conversation>> findById(@PathVariable Long id){
        try {
            Conversation conversation= conversationService.getConversationById(id);
            return  new ResponseEntity<>(ApiResponse.success(conversation), HttpStatus.OK);
        } catch (Exception exception){
            ApiResponse<Conversation> errorResponse = ApiResponse.error(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
            return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getByUser/{id}")
    public ResponseEntity<ApiResponse<?>> getByUserId(@PathVariable Long id){
        try {
            List<Conversation> conversations= conversationService.getConversationsByUserId(id);
            return  new ResponseEntity<>(ApiResponse.success(conversations), HttpStatus.OK);
        } catch (Exception exception){
            ApiResponse<Conversation> errorResponse = ApiResponse.error(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
            return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

//    @PostMapping("/add")
//    public ResponseEntity<ApiResponse<Conversation>> addConversation(@Valid @RequestBody Conversation conversation){
//        try {
//            Set<Long> userIds = conversation.getMembers().stream()
//                    .map(User::getId)
//                    .collect(Collectors.toSet());
//            return  new ResponseEntity<>(ApiResponse.success(
//                    conversationService.createConversation(conversation.getName(),false,userIds , conversation.getCreateBy().getId())), HttpStatus.CREATED);
//        } catch (Exception exception){
//            ApiResponse<Conversation> errorResponse = ApiResponse.error(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
//            return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//        }
//    }
@PostMapping("/add")
public ResponseEntity<ApiResponse<Conversation>> addConversation(@Valid @RequestBody Map<String, Object> payload){
    try {
        String name = (String) payload.get("name");
        List<Map<String, Integer>> membersPayload = (List<Map<String, Integer>>) payload.get("members"); // Có thể đây là vấn đề
        Long createdByUserId = ((Map<String, Integer>) payload.get("createBy")).get("id").longValue(); // Ép kiểu sang Long

        if (membersPayload == null || membersPayload.isEmpty()) {
            return new ResponseEntity<>(ApiResponse.error("Members list cannot be empty.", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }

        Set<Long> userIds = membersPayload.stream()
                .map(member -> member.get("id").longValue()) // Ép kiểu sang Long
                .collect(Collectors.toSet());

        return  new ResponseEntity<>(ApiResponse.success(
                conversationService.createConversation(name,false,userIds , createdByUserId)), HttpStatus.CREATED);
    } catch (ClassCastException e) {
        return new ResponseEntity<>(ApiResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    } catch (Exception exception){
        ApiResponse<Conversation> errorResponse = ApiResponse.error(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
        return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<Conversation>> updateConversation(@RequestBody Conversation conversation){
        try {
            return  new ResponseEntity<>(ApiResponse.success(conversationService.updateConversation(conversation)), HttpStatus.OK);
        } catch (Exception exception){
            ApiResponse<Conversation> errorResponse = ApiResponse.error(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
            return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> updateConversation(@PathVariable Long id){
        try {
            conversationService.deleteConversation(id);
            return  new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception){
            ApiResponse<Conversation> errorResponse = ApiResponse.error(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
            return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{conversationId}/addMember")
    public ResponseEntity<ApiResponse<?>> addMemberToConversation(@PathVariable Long conversationId, @RequestParam Long userId){
        try {

            return  new ResponseEntity<>(ApiResponse.success(conversationService.addUserToConversation(conversationId, userId)),HttpStatus.OK);
        } catch (Exception exception){
            ApiResponse<Conversation> errorResponse = ApiResponse.error(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
            return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{conversationId}/deleteMember")
    public ResponseEntity<ApiResponse<?>> removeMember(@PathVariable Long conversationId, @RequestParam Long userId){
        try {
            return  new ResponseEntity<>(ApiResponse.success(conversationService.removeUserFromConversation(conversationId, userId)),HttpStatus.OK);
        } catch (Exception exception){
            ApiResponse<Conversation> errorResponse = ApiResponse.error(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
            return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
