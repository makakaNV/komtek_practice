package com.lab.service.impl;

import com.lab.cache.impl.CacheServiceImpl;
import com.lab.dto.request.OrderRequestDTO;
import com.lab.dto.response.OrderResponseDTO;
import com.lab.dto.response.TestResponseDTO;
import com.lab.entity.Order;
import com.lab.entity.Patient;
import com.lab.entity.Status;
import com.lab.entity.Test;
import com.lab.exception.OrderNotFoundException;
import com.lab.exception.PatientNotFoundException;
import com.lab.exception.TestNotFoundException;
import com.lab.mapper.impl.OrderMapperImpl;
import com.lab.mapper.impl.TestMapperImpl;
import com.lab.repository.OrderRepository;
import com.lab.repository.PatientRepository;
import com.lab.repository.TestRepository;
import com.lab.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final TestRepository testRepository;
    private final PatientRepository patientRepository;
    private final OrderMapperImpl orderMapperImpl;
    private final TestMapperImpl testMapperImpl;
    private final CacheServiceImpl cacheServiceImpl;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            TestRepository testRepository,
            PatientRepository patientRepository,
            OrderMapperImpl orderMapperImpl,
            TestMapperImpl testMapperImpl,
            CacheServiceImpl cacheServiceImpl
    ) {
        this.orderRepository = orderRepository;
        this.testRepository = testRepository;
        this.patientRepository = patientRepository;
        this.orderMapperImpl = orderMapperImpl;
        this.testMapperImpl = testMapperImpl;
        this.cacheServiceImpl = cacheServiceImpl;
    }

    @Override
    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findAll(pageable);

        if (ordersPage.isEmpty()) {
            throw new OrderNotFoundException("Заявок не найдено");
        }

        return ordersPage.map(orderMapperImpl::toResponseDTO);
    }

    @Override
    @Cacheable(value = "orders", key = "#id")
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Заявки с id-" + id + " не найдено"));

        return orderMapperImpl.toResponseDTO(order);
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Patient patient = patientRepository.findById(orderRequestDTO.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException("Пациентов с id-"
                        + orderRequestDTO.getPatientId() + " не найдено"));

        Order order = orderMapperImpl.toEntity(orderRequestDTO, patient);

        order = orderRepository.save(order);

        return orderMapperImpl.toResponseDTO(order);
    }

    @Override
    @CachePut(value = "orders", key = "#orderId")
    public OrderResponseDTO updateOrderStatus(Long orderId, Status status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Заявок с id-" + orderId + " не найдено"));
        cacheServiceImpl.evictOrderCaches(order);
        order.setStatus(status);
        order = orderRepository.save(order);

        return orderMapperImpl.toResponseDTO(order);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByPatientId(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException("Пациент с id-" + patientId + " не найден");
        }

        List<Order> orders = orderRepository.findByPatientId(patientId);
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("Заявок у пациента с id-" + patientId + " не найдено");
        }
        return orderMapperImpl.toResponseDTOList(orders);
    }

    @Override
    public List<TestResponseDTO> getTestsByOrderId(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException("Заявка с id-" + orderId + " не найдена");
        }

        List<Test> tests = testRepository.findByOrderId(orderId);
        if (tests.isEmpty()) {
            throw new TestNotFoundException("Тестов для заявки с id-" + orderId + " не найдено");
        }
        return testMapperImpl.toResponseDTOList(tests);
    }

    @Override
    @Transactional
    @CacheEvict(value = "orders", key = "#id")
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Заявка с id-" + id + " не найдена"));

        List<Test> tests = testRepository.findByOrderId(order.getId());
        testRepository.deleteAll(tests);
        tests.forEach(cacheServiceImpl::evictTestCaches);

        cacheServiceImpl.evictOrderCaches(order);

        orderRepository.delete(order);
    }
}
