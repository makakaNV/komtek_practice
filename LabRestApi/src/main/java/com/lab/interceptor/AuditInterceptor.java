package com.lab.interceptor;


import com.lab.service.impl.JwtServiceImpl;
import javax.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Slf4j
@Component
public class AuditInterceptor implements HandlerInterceptor {

    private final JwtServiceImpl jwtService;

    public AuditInterceptor(JwtServiceImpl jwtService) {
        this.jwtService = jwtService;
    }

    private String extractUsername(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                return jwtService.extractUserName(token);
            } catch (Exception e) {
                return "unknown-user";
            }
        }
        return "anonymous";
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                @Nullable Exception ex) {

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        String username = extractUsername(request);
        int status = response.getStatus();

        log.info("Interceptor: [{}] {} запрос от пользователя {} к {} (IP: {}) => статус: {}{}",
                LocalDateTime.now(), method, username, uri, ip, status,
                ex != null ? (", ошибка: " + ex.getClass().getSimpleName()) : "");
    }

}

