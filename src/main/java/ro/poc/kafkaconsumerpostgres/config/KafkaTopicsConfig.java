package ro.poc.kafkaconsumerpostgres.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class KafkaTopicsConfig {

    @Value("${spring.kafka.boostrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.topics.documents}")
    private String documentsTopic;

    @Value("${spring.kafka.topics.orders}")
    private String ordersTopic;

    @Value("${spring.kafka.consumer-group}")
    private String consumerGroup;

    @Value("${spring.kafka.consumer-group-batch}")
    private String consumerGroupBatch;

}
