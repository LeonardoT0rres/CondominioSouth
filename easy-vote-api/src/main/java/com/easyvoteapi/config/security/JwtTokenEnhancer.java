package com.easyvoteapi.config.security;

import com.easyvoteapi.entities.User;
import com.easyvoteapi.repository.UserRepository;
import com.easyvoteapi.service.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenEnhancer implements TokenEnhancer {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UserNotFoundException("User invalido"));

        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        map.put("userName", user.getName());
        map.put("userEmail", user.getEmail());
        map.put("role", user.getRole().getName());
        map.put("userPasswordDefault", user.getPasswordDefault());

        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
        token.setAdditionalInformation(map);

        return accessToken;
    }
}