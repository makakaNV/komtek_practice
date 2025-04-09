package com.lab.service;

import com.lab.dto.request.NotificationRequestDTO;

public interface NotificationService {
    void notifyOrderStatusChanged(Long orderId, String message);
}
