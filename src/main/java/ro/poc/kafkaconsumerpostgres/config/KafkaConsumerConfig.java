package ro.poc.kafkaconsumerpostgres.config;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ro.poc.kafkaconsumerpostgres.model.DocumentModel;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;
import ro.poc.kafkaconsumerpostgres.model.OrderModel;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaTopicsConfig kafkaTopicsConfig;

    @Bean
    public ConsumerFactory<String, KafkaEvent<DocumentModel>> documentConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaTopicsConfig.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaTopicsConfig.getConsumerGroup());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, KafkaEventDeserializer.class.getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, KafkaEvent.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(new KafkaEventDeserializer<>(new TypeReference<>() {
                })));
    }

    @Bean
    public ConsumerFactory<String, KafkaEvent<OrderModel>> orderConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaTopicsConfig.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaTopicsConfig.getConsumerGroup());

        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new KafkaEventDeserializer<>(new TypeReference<>() {
                }));
    }

    @Bean
    public CommonErrorHandler commonErrorHandler() {
        return new KafkaEventErrorHandler();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaEvent<DocumentModel>> kafkaDocumentListenerContainerFactory(final CommonErrorHandler commonErrorHandler) {
        ConcurrentKafkaListenerContainerFactory<String, KafkaEvent<DocumentModel>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(documentConsumerFactory());
        factory.setCommonErrorHandler(commonErrorHandler);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaEvent<DocumentModel>> kafkaDocumentBatchListenerContainerFactory(final CommonErrorHandler commonErrorHandler) {
        ConcurrentKafkaListenerContainerFactory<String, KafkaEvent<DocumentModel>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(documentConsumerFactory());
        factory.setBatchListener(true);
        factory.setCommonErrorHandler(commonErrorHandler);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaEvent<OrderModel>> kafkaOrderBatchListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaEvent<OrderModel>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderConsumerFactory());
        factory.setBatchListener(true);
        return factory;
    }
}
