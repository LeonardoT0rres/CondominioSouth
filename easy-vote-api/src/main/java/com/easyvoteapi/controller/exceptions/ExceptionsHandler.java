package com.easyvoteapi.controller.exceptions;

import com.easyvoteapi.service.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@Slf4j
@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(AssemblyNotFoundException.class)
    public ResponseEntity<StandardError> assemblyNotFoundException(AssemblyNotFoundException e, HttpServletRequest request) {
        String errorMessage = "Assembleia não encontrada";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), errorMessage, e.getMessage(), request.getRequestURI());
        log.error(e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(AssemblyAlreadyExistsException.class)
    public ResponseEntity<StandardError> assemblyAlreadyExistsException(AssemblyAlreadyExistsException e, HttpServletRequest request) {
        String errorMessage = "Assembleia já cadastrada";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), errorMessage, e.getMessage(), request.getRequestURI());
        log.error(e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(InvalidAssemblyStatusException.class)
    public ResponseEntity<StandardError> invalidAssemblyStatusException(InvalidAssemblyStatusException e, HttpServletRequest request) {
        String errorMessage = "Status de assembleia inválido";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), errorMessage, e.getMessage(), request.getRequestURI());
        log.error(e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(InvalidAssemblyStartException.class)
    public ResponseEntity<StandardError> invalidAssemblyStartException(InvalidAssemblyStartException e, HttpServletRequest request) {
        String errorMessage = "Data de ínicio de assembleia inválida";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), errorMessage, e.getMessage(), request.getRequestURI());
        log.error(e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ScheduleNotFoundException.class)
    public ResponseEntity<StandardError> scheduleNotFoundException(ScheduleNotFoundException e, HttpServletRequest request) {
        String errorMessage = "Pauta não encontrada";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), errorMessage, e.getMessage(), request.getRequestURI());
        log.error(e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ScheduleAlreadyExistsException.class)
    public ResponseEntity<StandardError> scheduleAlreadyExistsException(ScheduleAlreadyExistsException e, HttpServletRequest request) {
        String errorMessage = "Pauta já cadastrada";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), errorMessage, e.getMessage(), request.getRequestURI());
        log.error(e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(InvalidScheduleStatusException.class)
    public ResponseEntity<StandardError> invalidScheduleStatusException(InvalidScheduleStatusException e, HttpServletRequest request) {
        String errorMessage = "Status de pauta inválido";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), errorMessage, e.getMessage(), request.getRequestURI());
        log.error(e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<StandardError> userNotFoundException(UserNotFoundException e, HttpServletRequest request) {
        String errorMessage = "Usuário não encontrado";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), errorMessage, e.getMessage(), request.getRequestURI());
        log.error(e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<StandardError> userUsernameAlreadyInUseException(UserAlreadyExistsException e, HttpServletRequest request) {
        String errorMessage = "Usuário já cadastrado";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), errorMessage, e.getMessage(), request.getRequestURI());
        log.error(e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(InvalidUserStatusException.class)
    public ResponseEntity<StandardError> invalidUserStatusException(InvalidUserStatusException e, HttpServletRequest request) {
        String errorMessage = "Status de usuário inválido";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), errorMessage, e.getMessage(), request.getRequestURI());
        log.error(e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(NonRedefinedPasswordException.class)
    public ResponseEntity<StandardError> nonRedefinedPasswordException(NonRedefinedPasswordException e, HttpServletRequest request) {
        String errorMessage = "Senha padrão não redefinida";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), errorMessage, e.getMessage(), request.getRequestURI());
        log.error(e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(AlreadyVotedException.class)
    public ResponseEntity<StandardError> alreadyVotedException(AlreadyVotedException e, HttpServletRequest request) {
        String errorMessage = "Voto já computado";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), errorMessage, e.getMessage(), request.getRequestURI());
        log.error(e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errorMessage = "Campo obrigatório não preenchido";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), errorMessage, e.getFieldError().getDefaultMessage(), request.getRequestURI());
        log.error(e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(EasterEggException.class)
    public ResponseEntity<StandardError> easterEggException(EasterEggException e, HttpServletRequest request) {
        String errorMessage = "O servidor recusa a tentativa de coar café num bule de chá";
        HttpStatus status = HttpStatus.I_AM_A_TEAPOT;
        StandardError err = new StandardError(Instant.now(), status.value(), errorMessage, e.getMessage(), request.getRequestURI());
        log.error(e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<StandardError> runtimeException(RuntimeException e, HttpServletRequest request) {
        String errorMessage = "Erro em tempo de execução";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), errorMessage, e.getMessage(), request.getRequestURI());
        log.error(e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> exception(Exception e, HttpServletRequest request) {
        String errorMessage = "Erro de compilação do código";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError err = new StandardError(Instant.now(), status.value(), errorMessage, e.getMessage(), request.getRequestURI());
        log.error(e.getMessage());
        return ResponseEntity.status(status).body(err);
    }
}
