package ro.poc.kafkaconsumerpostgres.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;
import ro.poc.kafkaconsumerpostgres.entity.Order;
import ro.poc.kafkaconsumerpostgres.entity.OrderEvent;
import ro.poc.kafkaconsumerpostgres.mapper.OrderMapper;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;
import ro.poc.kafkaconsumerpostgres.model.OrderModel;
import ro.poc.kafkaconsumerpostgres.repository.OrderEventRepository;
import ro.poc.kafkaconsumerpostgres.repository.OrderRepository;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventRepository orderEventRepository;

    @Transactional
    public void saveAll(final List<ConsumerRecord<String, KafkaEvent<OrderModel>>> records) {
        //todo: validators
        final Set<OrderEvent> orderEvents = records.stream()
                .filter(Objects::nonNull)
                .map(record -> OrderMapper.toOrderEvent(record.value()))
                .collect(Collectors.toSet());
        orderEventRepository.saveAll(orderEvents);
        final Set<Order> orders = records.stream()
                .filter(record -> Objects.nonNull(record) && Objects.nonNull(record.value()))
                .map(record -> OrderMapper.toOrder(record.value().getPayload()))
                .collect(Collectors.toSet());
        orderRepository.saveAll(orders);
    }
}
