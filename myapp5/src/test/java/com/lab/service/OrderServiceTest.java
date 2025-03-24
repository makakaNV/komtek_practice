package com.lab.service;

import com.lab.dto.OrderDTO;
import com.lab.dto.TestDTO;
import com.lab.entity.*;
import com.lab.exception.OrderNotFoundException;
import com.lab.exception.PatientNotFoundException;
import com.lab.exception.TestNotFoundException;
import com.lab.repository.OrderRepository;
import com.lab.repository.PatientRepository;
import com.lab.repository.TestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private TestRepository testRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllOrders_WhenOrdersExist_ShouldReturnListOfOrders(){
        Patient patient = new Patient(1L, "Иванов", "Иван", "Иванович", LocalDate.of(1990, 1, 1), Gender.MALE, null, null);
        Order order = new Order(1L, patient, LocalDateTime.now(), Status.REGISTERED, "Комментарий");
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderDTO> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Status.REGISTERED, result.get(0).getStatus());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getAllOrders_WhenOrdersDoesNotExist_ShouldThrowOrderNotFoundException() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(OrderNotFoundException.class, () -> orderService.getAllOrders());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void createOrder_WhenPatientExist_ShouldReturnCreatedOrderDTO() {
        Long patientId = 1L;
        Patient patient = new Patient(patientId, "Иванов", "Иван", "Иванович", LocalDate.of(1990, 1, 1), Gender.MALE, null, null);
        OrderDTO orderDTO = new OrderDTO(null, patientId, null, null, "Комментарий");
        Order savedOrder = new Order(1L, patient, LocalDateTime.now(), Status.REGISTERED, "Комментарий");
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        OrderDTO result = orderService.createOrder(orderDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Комментарий", result.getComment());
        verify(patientRepository, times(1)).findById(patientId);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_WhenPatientDoesNotExist_ShouldThrowPatientNotFoundException() {
        Long patientId = 999L;
        OrderDTO orderDTO = new OrderDTO(null, patientId, null, null, "Комментарий");
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> orderService.createOrder(orderDTO));
        verify(patientRepository, times(1)).findById(patientId);
    }

    @Test
    void updateOrderStatus_WhenOrderExist_ShouldUpdateOrderDTO() {
        Long orderId = 1L;
        Status newStatus = Status.COMPLETED;
        Patient patient = new Patient(1L, "Иванов", "Иван", "Иванович", LocalDate.of(1990, 1, 1), Gender.MALE, null, null);
        Order order = new Order(orderId, patient, LocalDateTime.now(), Status.REGISTERED, "Комментарий");
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDTO result = orderService.updateOrderStatus(orderId, newStatus);

        assertNotNull(result);
        assertEquals(newStatus, result.getStatus());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void updateOrderStatus_WhenOrderDoesNotExist_ShouldThrowOrderNotFoundException() {
        Long orderId = 999L;
        Status newStatus = Status.COMPLETED;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.updateOrderStatus(orderId, newStatus));
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void getOrderById_WhenOrderExists_ShouldReturnOrderDTO(){
        Long orderId = 1L;
        Patient patient = new Patient(1L, "Иванов", "Иван", "Иванович", LocalDate.of(1990, 1, 1), Gender.MALE, null, null);
        Order order = new Order(orderId, patient, LocalDateTime.now(), Status.REGISTERED, "Комментарий");
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        OrderDTO result = orderService.getOrderById(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals("Комментарий", result.getComment());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void getOrderById_WhenOrderDoesNotExists_ShouldReturnOrderDTO(){
        Long orderId = 999L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(orderId));
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void getOrdersByPatientId_WhenOrdersExist_ShouldReturnListOfOrderDTOs() {
        Long patientId = 1L;
        Patient patient = new Patient(patientId, "Иванов", "Иван", "Иванович", LocalDate.of(1990, 1, 1), Gender.MALE, null, null);
        Order order = new Order(1L, patient, LocalDateTime.now(), Status.REGISTERED, "Комментарий");
        when(patientRepository.existsById(patientId)).thenReturn(true);
        when(orderRepository.findByPatientId(patientId)).thenReturn(List.of(order));

        List<OrderDTO> result = orderService.getOrdersByPatientId(patientId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Комментарий", result.get(0).getComment());
        verify(patientRepository, times(1)).existsById(patientId);
        verify(orderRepository, times(1)).findByPatientId(patientId);
    }

    @Test
    void getOrdersByPatientId_WhenOrdersDoesNotExist_ShouldReturnListOfOrderDTOs() {
        Long patientId = 1L;
        when(patientRepository.existsById(patientId)).thenReturn(true);
        when(orderRepository.findByPatientId(patientId)).thenReturn(Collections.emptyList());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrdersByPatientId(patientId));
        verify(patientRepository, times(1)).existsById(patientId);
        verify(orderRepository, times(1)).findByPatientId(patientId);
    }

    @Test
    void getOrdersByPatientId_WhenPatientDoesNotExist_ShouldReturnListOfOrderDTOs() {
        Long patientId = 999L;
        when(patientRepository.existsById(patientId)).thenReturn(false);

        assertThrows(PatientNotFoundException.class, () -> orderService.getOrdersByPatientId(patientId));
        verify(patientRepository, times(1)).existsById(patientId);
    }

    @Test
    void getTestsByOrderId_WhenTestsExist_ShouldReturnListOfTestDTOs() {
        Long orderId = 1L;
        Long testTypeId = 1L;
        Patient patient = new Patient(1L, "Иванов", "Иван", "Иванович", LocalDate.of(1990, 1, 1), Gender.MALE, null, null);
        Order order = new Order(orderId, patient, LocalDateTime.now(), Status.REGISTERED, "Комментарий");
        TestType testType = new TestType(testTypeId, "Анализ крови", "AN-001", "Описание анализа крови", new BigDecimal("100.00"));
        com.lab.entity.Test test = new com.lab.entity.Test(1L, order, testType, LocalDateTime.now(), "N/A", "refval", TestStatus.COMPLETED);

        when(orderRepository.existsById(orderId)).thenReturn(true);
        when(testRepository.findByOrderId(orderId)).thenReturn(List.of(test));


        List<TestDTO> result = orderService.getTestsByOrderId(orderId);

        assertNotNull(result);
        assertEquals(1, result.size());
        TestDTO testDTO = result.get(0);
        assertEquals("N/A", testDTO.getResult());
        assertEquals("refval", testDTO.getReferenceValues());
        assertEquals("Анализ крови", testType.getName());
        assertEquals(testTypeId, testDTO.getTestTypeId());

        verify(orderRepository, times(1)).existsById(orderId);
        verify(testRepository, times(1)).findByOrderId(orderId);
    }

    @Test
    void getTestsByOrderId_WhenOrderDoesNotExist_ShouldThrowOrderNotFoundException() {
        Long orderId = 999L;
        when(orderRepository.existsById(orderId)).thenReturn(false);

        assertThrows(OrderNotFoundException.class, () -> orderService.getTestsByOrderId(orderId));
        verify(orderRepository, times(1)).existsById(orderId);
    }

    @Test
    void getTestsByOrderId_WhenTestsDoNotExist_ShouldThrowTestNotFoundException() {
        Long orderId = 1L;
        when(orderRepository.existsById(orderId)).thenReturn(true);
        when(testRepository.findByOrderId(orderId)).thenReturn(Collections.emptyList());

        assertThrows(TestNotFoundException.class, () -> orderService.getTestsByOrderId(orderId));
        verify(orderRepository, times(1)).existsById(orderId);
        verify(testRepository, times(1)).findByOrderId(orderId);
    }

    @Test
    void deleteOrder_WhenOrderExists_ShouldDeleteOrder() {
        Long orderId = 1L;
        Order order = new Order(orderId, null, LocalDateTime.now(), Status.REGISTERED, "Комментарий");
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void deleteOrder_WhenOrderDoesNotExist_ShouldThrowOrderNotFoundException() {
        Long orderId = 999L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(orderId));
        verify(orderRepository, times(1)).findById(orderId);
    }
}
