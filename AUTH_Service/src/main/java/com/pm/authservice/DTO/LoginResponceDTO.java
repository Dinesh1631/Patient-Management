package com.pm.authservice.DTO;

public class LoginResponceDTO {

    private final String accessToken;

    public LoginResponceDTO(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
