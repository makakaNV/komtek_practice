package com.lab.controller;

import com.lab.dto.request.SignInRequestDTO;
import com.lab.dto.request.SignUpRequestDTO;
import com.lab.dto.response.JwtAuthenticationResponseDTO;
import com.lab.exception.ErrorResponse;
import com.lab.service.impl.AuthenticationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Регистрация/авторизация")
public class AuthController {
    private final AuthenticationServiceImpl authenticationService;

    @PostMapping("/sign-up")
    @SuppressWarnings("unused")
    @Operation (
            summary = "Регистрация пользователя",
            description = "Регистрация нового пользователя по username, email, password"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Регистрация пользователя. Выдача токена",
            content = @Content(schema = @Schema(implementation = JwtAuthenticationResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Ошибка валидации полей",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public JwtAuthenticationResponseDTO signUp(@RequestBody @Valid SignUpRequestDTO request) {
        return authenticationService.signUp(request);
    }


    @PostMapping("/sign-in")
    @SuppressWarnings("unused")
    @Operation(
            summary = "Аутентификация пользователя",
            description = "Аутентификация пользователя по username, password"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Аутентификация пользователя. Выдача токена",
            content = @Content(schema = @Schema(implementation = JwtAuthenticationResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Ошибка валидации полей",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public JwtAuthenticationResponseDTO signIn(@RequestBody @Valid SignInRequestDTO request) {
        return authenticationService.signIn(request);
    }
}
