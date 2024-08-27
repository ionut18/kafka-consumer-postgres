package ro.poc.kafkaconsumerpostgres.messaging;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ro.poc.kafkaconsumerpostgres.config.KafkaTopicsConfig;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;
import ro.poc.kafkaconsumerpostgres.model.OrderModel;
import ro.poc.kafkaconsumerpostgres.service.OrderService;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class OrderConsumer {

    private final OrderService orderService;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    @KafkaListener(topics = "#{kafkaTopicsConfig.getOrdersTopic()}",
            groupId = "#{kafkaTopicsConfig.getConsumerGroupBatch()}",
            containerFactory = "kafkaOrderBatchListenerContainerFactory")
    public void consumeBatchOrders(final List<ConsumerRecord<String, KafkaEvent<OrderModel>>> records) {
        log.info("[{}] Consuming {} messages", kafkaTopicsConfig.getConsumerGroupBatch(), records.size());
        try {
            orderService.saveAll(records);
        } catch (Exception e) {
            log.error("[{}] Error consuming message {}", kafkaTopicsConfig.getConsumerGroupBatch(), e.getMessage(), e);
        }
        log.info("[{}] Finished consuming {} messages", kafkaTopicsConfig.getConsumerGroupBatch(), records.size());
    }
}
