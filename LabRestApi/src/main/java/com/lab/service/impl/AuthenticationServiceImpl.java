package com.lab.service.impl;

import com.lab.dto.request.SignInRequestDTO;
import com.lab.dto.request.SignUpRequestDTO;
import com.lab.dto.response.JwtAuthenticationResponseDTO;
import com.lab.entity.Role;
import com.lab.entity.User;
import com.lab.exception.InvalidPasswordException;
import com.lab.exception.UserNotFoundException;
import com.lab.repository.UserRepository;
import com.lab.service.AuthenticationService;
import com.lab.service.JwtService;
import com.lab.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    //private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    @Override
    public JwtAuthenticationResponseDTO signUp(SignUpRequestDTO request) {

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userService.create(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponseDTO(jwt);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    @Override
    public JwtAuthenticationResponseDTO signIn(SignInRequestDTO request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Неверный пароль");
        }

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponseDTO(jwt);
    }
}
