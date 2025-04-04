package com.lab.service;

import com.lab.dto.request.SignInRequestDTO;
import com.lab.dto.request.SignUpRequestDTO;
import com.lab.dto.response.JwtAuthenticationResponseDTO;

public interface AuthenticationService {
    JwtAuthenticationResponseDTO signUp(SignUpRequestDTO request);
    JwtAuthenticationResponseDTO signIn(SignInRequestDTO request);
}
