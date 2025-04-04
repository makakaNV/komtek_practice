package com.lab.mapper.impl;

import com.lab.dto.request.OrderRequestDTO;
import com.lab.dto.response.OrderResponseDTO;
import com.lab.entity.Order;
import com.lab.entity.Patient;
import com.lab.mapper.OrderMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderResponseDTO toResponseDTO(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .patientId(order.getPatient().getId())
                .createdDate(order.getCreatedDate())
                .status(order.getStatus())
                .comment(order.getComment())
                .build();
    }

    @Override
    public List<OrderResponseDTO> toResponseDTOList(List<Order> orders) {
        return orders.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Order toEntity(OrderRequestDTO dto, Patient patient) {
        return Order.builder()
                .patient(patient)
                .status(dto.getStatus())
                .comment(dto.getComment())
                .createdDate(LocalDateTime.now())
                .build();
    }
}
