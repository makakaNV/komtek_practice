package com.lab.service;

import com.lab.dto.request.OrderRequestDTO;
import com.lab.dto.response.OrderResponseDTO;
import com.lab.dto.response.TestResponseDTO;
import com.lab.entity.*;
import com.lab.exception.OrderNotFoundException;
import com.lab.exception.PatientNotFoundException;
import com.lab.mapper.OrderMapper;
import com.lab.mapper.impl.TestMapperImpl;
import com.lab.repository.OrderRepository;
import com.lab.repository.PatientRepository;
import com.lab.repository.TestRepository;
import com.lab.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private TestRepository testRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private TestMapperImpl testMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private final Patient patient = new Patient(
            1L,
            "Иван",
            "Иванов",
            "Иванович",
            LocalDate.of(1970, 1, 1),
            Gender.MALE,
            "+79028518877",
            "123"
    );
    private final Order order = new Order(
            1L,
            patient,
            LocalDateTime.now(),
            Status.REGISTERED,
            "Комментарий"
    );
    private final com.lab.entity.Test test = new com.lab.entity.Test(
            1L,
            order,
            new TestType(),
            LocalDateTime.now(),
            "Результат",
            "Референсные значения",
            TestStatus.COMPLETED
    );

    private final OrderRequestDTO orderRequestDTO = new OrderRequestDTO(
            1L,
            Status.REGISTERED,
            "Комментарий"
    );
    private final OrderResponseDTO orderResponseDTO = new OrderResponseDTO(
            1L,
            1L,
            LocalDateTime.now(),
            Status.REGISTERED,
            "Комментарий"
    );
    private final TestResponseDTO testResponseDTO = new TestResponseDTO(
            1L,
            1L,
            1L,
            LocalDateTime.now(),
            "Результат",
            "Референсные значения",
            TestStatus.COMPLETED);


    @Test
     void getAllOrders_ShouldReturnOrders_WhenOrdersExist() {
        Page<Order> page = new PageImpl<>(List.of(order));
        when(orderRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(orderMapper.toResponseDTOList(anyList())).thenReturn(List.of(orderResponseDTO));

        Page<OrderResponseDTO> result = orderService.getAllOrders(Pageable.unpaged());

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        verify(orderRepository).findAll(any(Pageable.class));
    }

    @Test
    void getAllOrders_ShouldThrowException_WhenNoOrdersFound() {
        when(orderRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        assertThrows(OrderNotFoundException.class, () ->
                orderService.getAllOrders(Pageable.unpaged()));
    }

    @Test
    void getOrderById_ShouldReturnOrder_WhenOrderExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toResponseDTO(any(Order.class))).thenReturn(orderResponseDTO);

        OrderResponseDTO result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getOrderById_ShouldThrowException_WhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () ->
                orderService.getOrderById(1L));
    }

    @Test
    void createOrder_ShouldCreateOrder_WhenPatientExists() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(orderMapper.toEntity(any(OrderRequestDTO.class), any(Patient.class))).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDTO(any(Order.class))).thenReturn(orderResponseDTO);

        OrderResponseDTO result = orderService.createOrder(orderRequestDTO);

        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_ShouldThrowException_WhenPatientNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () ->
                orderService.createOrder(orderRequestDTO));
    }

    @Test
    void updateOrderStatus_ShouldUpdateStatus_WhenOrderExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDTO(any(Order.class))).thenReturn(orderResponseDTO);

        OrderResponseDTO result = orderService.updateOrderStatus(1L, Status.IN_PROGRESS);

        assertNotNull(result);
        verify(orderRepository).save(order);
        assertEquals(Status.IN_PROGRESS, order.getStatus());
    }

    @Test
    void getOrdersByPatientId_ShouldReturnOrders_WhenPatientAndOrdersExist() {
        when(patientRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.findByPatientId(1L)).thenReturn(List.of(order));
        when(orderMapper.toResponseDTOList(anyList())).thenReturn(List.of(orderResponseDTO));

        List<OrderResponseDTO> result = orderService.getOrdersByPatientId(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getOrdersByPatientId_ShouldThrowException_WhenPatientNotFound() {
        when(patientRepository.existsById(1L)).thenReturn(false);

        assertThrows(PatientNotFoundException.class, () ->
                orderService.getOrdersByPatientId(1L));
    }

    @Test
    void getTestsByOrderId_ShouldReturnTests_WhenOrderAndTestsExist() {
        when(orderRepository.existsById(1L)).thenReturn(true);
        when(testRepository.findByOrderId(1L)).thenReturn(List.of(test));
        when(testMapper.toResponseDTOList(anyList())).thenReturn(List.of(testResponseDTO));

        List<TestResponseDTO> result = orderService.getTestsByOrderId(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void deleteOrder_ShouldDeleteOrder_WhenOrderExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        doNothing().when(orderRepository).delete(any(Order.class));

        orderService.deleteOrder(1L);

        verify(orderRepository).delete(order);
    }

    @Test
    void deleteOrder_ShouldThrowException_WhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () ->
                orderService.deleteOrder(1L));
    }
}