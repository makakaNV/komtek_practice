package com.lab.service.impl;

import com.lab.dto.request.NotificationRequestDTO;
import com.lab.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final RestTemplate restTemplate;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    @Override
    public void notifyOrderStatusChanged(Long orderId, String message) {
        NotificationRequestDTO requestDTO = NotificationRequestDTO.builder()
                .orderId(orderId)
                .message(message)
                .build();

        restTemplate.postForEntity(
                notificationServiceUrl,
                requestDTO,
                Void.class
        );
    }
}
