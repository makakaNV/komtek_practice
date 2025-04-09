package com.lab.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.exception.ErrorResponse;
import com.lab.service.impl.JwtServiceImpl;
import com.lab.service.impl.UserServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    private final JwtServiceImpl jwtService;
    private final UserServiceImpl userService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        var authHeader = request.getHeader(HEADER_NAME);

        if (StringUtils.isEmpty(authHeader)) {
            if (isSecuredEndpoint(request)) {
                sendError(response, "Требуется авторизация. Предоставьте токен.", HttpStatus.UNAUTHORIZED);
                return;
            }
            filterChain.doFilter(request, response);
            return;
        }

        if (!StringUtils.startsWith(authHeader, BEARER_PREFIX)) {
            sendError(response, "Неверный формат токена. Используйте формат 'Bearer <токен>'.", HttpStatus.UNAUTHORIZED);
            return;
        }

        try {
            var jwt = authHeader.substring(BEARER_PREFIX.length());

            var username = jwtService.extractUserName(jwt);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);

        } catch (MalformedJwtException e) {
            sendError(response, "Токен не существует", HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            sendError(response, "Токен просрочен. Пожалуйста, авторизуйтесь снова.", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            sendError(response, "Ошибка аутентификации: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean isSecuredEndpoint(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/v1/auth/")
                && !request.getRequestURI().startsWith("/swagger-ui/")
                && !request.getRequestURI().startsWith("/v3/api-docs/")
                && !request.getRequestURI().startsWith("/api/v1/notifications");
    }

    private void sendError(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ErrorResponse errorResponse = new ErrorResponse(message, status.value());
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}