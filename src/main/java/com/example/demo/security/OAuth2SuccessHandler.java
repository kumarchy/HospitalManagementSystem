package com.example.demo.security;

import com.example.demo.dto.LoginResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
//@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
//    private final AuthService authService;
//    private final ObjectMapper objectMapper;

    private final AuthService authService;
    private final ObjectMapper objectMapper;

    public OAuth2SuccessHandler(@Lazy AuthService authService, ObjectMapper objectMapper) {
        this.authService = authService;
        this.objectMapper = objectMapper;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String registrationId = token.getAuthorizedClientRegistrationId(); // this is the registration id which indicates that whether the user is logged in using google or github

        // this method will handle login request
        ResponseEntity<LoginResponseDto>loginResponse = authService.handleOAuth2LoginRequest(oAuth2User, registrationId);

        // we will get modified response in frontend
        response.setStatus(loginResponse.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // it return json value to frontend
        response.getWriter().write(objectMapper.writeValueAsString(loginResponse.getBody())); // it will change the body to json value and will get json value format in frontend
    }
}
