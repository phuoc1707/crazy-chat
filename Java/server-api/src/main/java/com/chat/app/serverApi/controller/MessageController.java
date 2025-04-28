package com.chat.app.serverApi.controller;

import com.chat.app.serverApi.entity.Message;
import com.chat.app.serverApi.entity.User;
import com.chat.app.serverApi.exceptionHandler.ApiResponse;
import com.chat.app.serverApi.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    MessageService messageService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<?>> addMessage(@RequestBody Map<String, Object> payload){
        try {

            String message = (String) payload.get("message");
            List<String> urls = (List<String>) payload.get("urls");
            Map<String, Long> sender = (Map<String, Long>) payload.get("sender");
            Map<String, Long> conversation = (Map<String, Long>) payload.get("conversation");

            return new ResponseEntity<>(ApiResponse.success(
                    messageService.saveMessage(((Number) sender.get("id")).longValue()
                            ,((Number) conversation.get("id")).longValue()
                            ,message
                            ,urls.stream().collect(Collectors.toSet())))
                    , HttpStatus.CREATED);
        } catch (RuntimeException exception){
            ApiResponse<User> errorResponse = ApiResponse.error(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
            return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getByConversation/{id}")
    public ResponseEntity<ApiResponse<?>> addMessage(@PathVariable Long id){
        try {

            return new ResponseEntity<>(ApiResponse.success(messageService.getMessagesByConversation(id)), HttpStatus.CREATED);
        } catch (RuntimeException exception){
            ApiResponse<User> errorResponse = ApiResponse.error(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
            return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

}
