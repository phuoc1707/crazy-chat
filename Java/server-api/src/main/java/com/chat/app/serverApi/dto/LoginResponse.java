package com.chat.app.serverApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private UserInfo user;

    @Data
    @AllArgsConstructor
    public static class UserInfo {
        private String userName;
        private String avatar;
        private String lastName;
        private String firstName;
    }
}
