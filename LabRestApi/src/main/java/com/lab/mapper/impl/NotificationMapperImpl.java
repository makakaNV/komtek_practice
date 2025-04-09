package com.lab.mapper.impl;

import com.lab.dto.request.NotificationRequestDTO;
import com.lab.dto.response.NotificationResponseDTO;
import com.lab.entity.Notification;
import com.lab.mapper.NotificationMapper;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public Notification toEntity(NotificationRequestDTO dto) {
        return Notification.builder()
                .orderId(dto.getOrderId())
                .message(dto.getMessage())
                .build();
    }

    @Override
    public NotificationResponseDTO toResponseDTO(Notification notification) {
        return NotificationResponseDTO.builder()
                .id(notification.getId())
                .orderId(notification.getOrderId())
                .message(notification.getMessage())
                .timestamp(notification.getTimestamp())
                .build();
    }
}
