package com.easyvoteapi.service;

import com.easyvoteapi.entities.User;
import com.easyvoteapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User authenticated() {
        var auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        final var userEmail = auth.getName();

        var result = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UnauthorizedUserException("Usuário de email '" + userEmail + "' não encontrado!"));

        log.info("Autenticação do email '" + userEmail + "' verificada!");

        return result;
    }
}