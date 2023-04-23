package br.com.fiap.abctechapi.service;

import br.com.fiap.abctechapi.entity.Assistance;
import br.com.fiap.abctechapi.entity.Order;
import br.com.fiap.abctechapi.handler.exception.MaximumAssistException;
import br.com.fiap.abctechapi.handler.exception.MinimumAssistRequiredException;
import br.com.fiap.abctechapi.repository.AssistanceRepository;
import br.com.fiap.abctechapi.repository.OrderRepository;
import br.com.fiap.abctechapi.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class OrderServiceTest {

    @MockBean
    private AssistanceRepository assistanceRepository;

    @MockBean
    private OrderRepository orderRepository;

    private OrderService orderService;
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderServiceImpl(assistanceRepository,orderRepository);
        Mockito.when(assistanceRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new Assistance(1L, "Teste", "Teste Description")));
    }

    //Minimum assistencias > 0
    @Test
    public void create_order_error_min_assist(){

        Order newOrder = new Order();
        newOrder.setOperatorId(1234L);
        Assertions.assertThrows(MinimumAssistRequiredException.class, () -> orderService.saveOrder(newOrder, List.of()));
        Mockito.verify(orderRepository, Mockito.times(0)).save(newOrder);
    }

    //Maximo assistencias <= 15
    @Test
    public void create_order_error_max_assist(){

        Order newOrder = new Order();
        newOrder.setOperatorId(1234L);
        Assertions.assertThrows(MaximumAssistException.class, () -> orderService.saveOrder(newOrder, List.of(1L, 2L, 3L, 4L, 5L,6L, 7L, 1L, 2L, 3L, 4L, 5L,6L, 7L, 1L,2L)));
        Mockito.verify(orderRepository, Mockito.times(0)).save(newOrder);
    }

    // Create Order Service
    @Test
    public void create_order_success() {

        Order newOrder = new Order();
        newOrder.setOperatorId(1234L);

        List<Long> assistsId = List.of(1L, 2L);

        Mockito.when(orderRepository.save(newOrder)).thenReturn(newOrder);
        Mockito.when(assistanceRepository.findAllById(assistsId))
                .thenReturn(List.of(new Assistance(1L, "Assistance 1", "Description 1"),
                        new Assistance(2L, "Assistance 2", "Description 2")));

        Assertions.assertDoesNotThrow(() -> orderService.saveOrder(newOrder, assistsId));
    }
}