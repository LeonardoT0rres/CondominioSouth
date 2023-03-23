package com.easyvoteapi.service;

import com.easyvoteapi.dto.UserDto;
import com.easyvoteapi.dto.UserRequestDto;
import com.easyvoteapi.repository.RoleRepository;
import com.easyvoteapi.repository.UserRepository;
import com.easyvoteapi.service.exceptions.InvalidUserStatusException;
import com.easyvoteapi.service.exceptions.NonRedefinedPasswordException;
import com.easyvoteapi.service.exceptions.UserAlreadyExistsException;
import com.easyvoteapi.service.exceptions.UserNotFoundException;
import com.easyvoteapi.utils.constants.MapperConstants;
import com.easyvoteapi.utils.enums.Status;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static final Long CONDOMINO = 1L;
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final AuthService authService;

    private BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public UserDto create(UserRequestDto userRequestDto) {
        var optUser = repository.findByNameOrEmailAndStatus
                (userRequestDto.getName(), userRequestDto.getEmail(), Status.ATIVO.name());

        if (optUser.isPresent())
            throw new UserAlreadyExistsException("Usuário já cadastrado, os campos 'Nome' e/ou 'Email' já estão sendo utilizados!");

        var user = MapperConstants.userMapper
                .requestToEntity(userRequestDto);

        user.setRole(roleRepository.findById(CONDOMINO).get());
        user.setStatus(Status.ATIVO);

        var password = new Faker()
                .internet()
                .password(12, 13, true, true);

        user.setPassword(passwordEncoder()
                .encode(password));

        var result = MapperConstants.userMapper
                .toDto(repository.save(user))
                .withPasswordMessage("Senha temporária: " + password);

        log.info("Usuário '" + userRequestDto.getName() + "' criado!");

        return result;
    }

    public List<UserDto> findAll() {
        var result = repository.findAll()
                .stream()
                .filter(userDto -> userDto.getStatus().equals(Status.ATIVO))
                .map(MapperConstants.userMapper::toDto)
                .collect(Collectors.toList());

        log.info("Lista de usuários retornada!");

        return result;
    }

    public UserDto findById(Long id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário de id " + id + " não encontrado!"));

        var result = MapperConstants.userMapper
                .toDto(user);

        log.info("Usuário '" + user.getName() + "' retornado!");

        return result;
    }

    public UserDto update(UserRequestDto userRequestDto) {
        var user = repository.findById(authService.authenticated().getId())
                .orElseThrow(() -> new UserNotFoundException("Usuário de id " + authService.authenticated().getId() + " não encontrado!"));

        var existUser = repository.findByNameOrEmailAndStatusIdIsNot
                (userRequestDto.getName(), userRequestDto.getEmail(), Status.ATIVO.name(), authService.authenticated().getId());

        if (existUser.isPresent())
            throw new UserAlreadyExistsException("Usuário já cadastrado, os campos 'Nome' e/ou 'Email' já estão sendo utilizados!");
        else if (user.getStatus().equals(Status.INATIVO))
            throw new InvalidUserStatusException("Status '" + user.getStatus().name() + "' do usuário de id " + authService.authenticated().getId() + " inválido para a edição!");

        user.setName(userRequestDto.getName() == null ? user.getName() : userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail() == null ? user.getEmail() : userRequestDto.getEmail());
        user.setApartment(userRequestDto.getApartment() == null ? user.getApartment() : userRequestDto.getApartment());

        if (userRequestDto.getPassword() == null) {
            throw new NonRedefinedPasswordException("Não é permitido manter a senha padrão, por favor altere ela!");
        }
        var pass = passwordEncoder().encode(userRequestDto.getPassword());

        if (user.getPassword().equals(pass) && user.getPasswordDefault() == 1)
            throw new NonRedefinedPasswordException("Não é permitido usar a senha padrão, por favor altere ela!");

        user.setPasswordDefault(0);
        user.setPassword(pass);

        var result = MapperConstants.userMapper
                .toDto(repository.save(user));

        log.info("Usuário de id " + authService.authenticated().getId() + " editado!");

        return result;
    }

    public void delete(Long id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário de id " + id + " não encontrado!"));

        if (user.getStatus().equals(Status.ATIVO)) {
            user.setStatus(Status.INATIVO);

            repository.save(user);

            log.info("Usuário '" + user.getName() + "' deletado!");
        } else {
            throw new InvalidUserStatusException("Status '" + user.getStatus().name() + "' do usuário de id " + id + " inválido para a deleção!");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email '" + email + "' não encontrado!"));

        log.info("Usuário encontrado: " + email);

        return user;
    }
}
