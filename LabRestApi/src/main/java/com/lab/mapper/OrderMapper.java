package com.lab.mapper;

import com.lab.dto.request.OrderRequestDTO;
import com.lab.dto.response.OrderResponseDTO;
import com.lab.entity.Order;
import com.lab.entity.Patient;

import java.util.List;

public interface OrderMapper {
    OrderResponseDTO toResponseDTO(Order order);
    List<OrderResponseDTO> toResponseDTOList(List<Order> orders);
    Order toEntity(OrderRequestDTO dto, Patient patient);
}
