package com.lab.mapper;

import com.lab.dto.request.NotificationRequestDTO;
import com.lab.dto.response.NotificationResponseDTO;
import com.lab.entity.Notification;

public interface NotificationMapper {
    Notification toEntity(NotificationRequestDTO dto);
    NotificationResponseDTO toResponseDTO(Notification notification);
}
