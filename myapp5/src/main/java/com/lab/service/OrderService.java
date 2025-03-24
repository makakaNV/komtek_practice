package com.lab.service;

import com.lab.dto.OrderDTO;
import com.lab.dto.TestDTO;
import com.lab.entity.Order;
import com.lab.entity.Patient;
import com.lab.entity.Status;
import com.lab.entity.Test;
import com.lab.exception.OrderNotFoundException;
import com.lab.exception.PatientNotFoundException;
import com.lab.exception.TestNotFoundException;
import com.lab.repository.OrderRepository;
import com.lab.repository.PatientRepository;
import com.lab.repository.TestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private PatientRepository patientRepository;


    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("Заявок не найдено");
        }
        return orders.stream()
                .map(order -> new OrderDTO(order))
                .collect(Collectors.toList());
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        Patient patient = patientRepository.findById(orderDTO.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException("Пациентов с id-" + orderDTO.getPatientId() + " не найдено"));

        Order order = new Order();
        order.setPatient(patient);
        order.setCreatedDate(LocalDateTime.now());
        order.setStatus(orderDTO.getStatus());
        order.setComment(orderDTO.getComment());

        order = orderRepository.save(order);

        return new OrderDTO(order.getId(), order.getPatient().getId(), order.getCreatedDate(), order.getStatus(), order.getComment());
    }

    public OrderDTO updateOrderStatus(Long orderId, Status status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Заявок с id-" + orderId + " не найдено"));

        order.setStatus(status);
        order = orderRepository.save(order);

        return new OrderDTO(order.getId(), order.getPatient().getId(), order.getCreatedDate(), order.getStatus(), order.getComment());
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Заявки с id-" + id + " не найдено"));

        return new OrderDTO(order.getId(), order.getPatient().getId(), order.getCreatedDate(), order.getStatus(), order.getComment());
    }

    public List<OrderDTO> getOrdersByPatientId(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException("Пациент с id-" + patientId + " не найден");
        }

        List<Order> orders = orderRepository.findByPatientId(patientId);
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("Заявок у пациента с id-" + patientId + " не найдено");
        }
        return orders.stream()
                .map(order -> new OrderDTO(order))
                .collect(Collectors.toList());
    }

    public List<TestDTO> getTestsByOrderId(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException("Заявка с id-" + orderId + " не найдена");
        }

        List<Test> tests = testRepository.findByOrderId(orderId);
        if (tests.isEmpty()) {
            throw new TestNotFoundException("Тестов для заявки с id-" + orderId + " не найдено");
        }
        return tests.stream()
                .map(test -> new TestDTO(test))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Заявка с id-" + id + " не найдена"));
        orderRepository.delete(order);
    }
}
