package com.lab.service;


import com.lab.dto.request.OrderRequestDTO;
import com.lab.dto.response.OrderResponseDTO;
import com.lab.dto.response.TestResponseDTO;
import com.lab.entity.Status;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface OrderService {
    List<OrderResponseDTO> getAllOrders(Pageable pageable);
    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO);
    OrderResponseDTO updateOrderStatus(Long orderId, Status status);
    OrderResponseDTO getOrderById(Long id);
    List<OrderResponseDTO> getOrdersByPatientId(Long patientId);
    List<TestResponseDTO> getTestsByOrderId(Long orderId);
    void deleteOrder(Long id);
}
