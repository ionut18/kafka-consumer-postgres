package ro.poc.kafkaconsumerpostgres.messaging;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import ro.poc.kafkaconsumerpostgres.config.KafkaTopicsConfig;
import ro.poc.kafkaconsumerpostgres.model.DocumentModel;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;
import ro.poc.kafkaconsumerpostgres.service.DocumentService;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Validated
public class DocumentConsumer {

    private final DocumentService documentService;
    private final KafkaTopicsConfig kafkaTopicsConfig;
    private final Validator validator;

    @KafkaListener(topics = "#{kafkaTopicsConfig.getDocumentsTopic()}",
            containerFactory = "kafkaDocumentListenerContainerFactory")
    public void consume(final ConsumerRecord<String, @Valid KafkaEvent<DocumentModel>> record,
                        @Header(KafkaHeaders.GROUP_ID) final String groupId) {
        final KafkaEvent<DocumentModel> kafkaEvent = record.value();
        final BindingResult result = new BeanPropertyBindingResult(kafkaEvent, "kafkaEvent");
        validator.validate(kafkaEvent, result);
        if (result.hasErrors()) {
            log.error("[{}] Validation errors: {}", groupId, result.getAllErrors());
            return;
        }
        log.debug("[{}] Consuming message {}", groupId, kafkaEvent);
        try {
            documentService.save(record.key(), record.value());
        } catch (Exception e) {
            log.error("[{}] Error consuming message {}", groupId, e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "#{kafkaTopicsConfig.getDocumentsTopic()}",
            groupId = "#{kafkaTopicsConfig.getConsumerGroupBatch()}",
            containerFactory = "kafkaDocumentBatchListenerContainerFactory")
    public void consumeBatch(final List<ConsumerRecord<String, @Valid KafkaEvent<DocumentModel>>> records) {
        log.debug("[{}] Consuming {} messages", kafkaTopicsConfig.getConsumerGroupBatch(), records.size());
        final List<ConsumerRecord<String, KafkaEvent<DocumentModel>>> validRecords = new ArrayList<>();
        try {
            records.forEach(record -> {
                final KafkaEvent<DocumentModel> kafkaEvent = record.value();
                final BindingResult result = new BeanPropertyBindingResult(kafkaEvent, "kafkaEvent");
                validator.validate(kafkaEvent, result);
                if (result.hasErrors()) {
                    log.error("[{}] Validation errors: {}", kafkaTopicsConfig.getConsumerGroupBatch(), result.getAllErrors());
                } else {
                    validRecords.add(record);
                }
                log.debug("[{}] Consuming message {}", kafkaTopicsConfig.getConsumerGroupBatch(), record);
            });
            documentService.saveAll(validRecords);
        } catch (Exception e) {
            log.error("[{}] Error consuming message {}", kafkaTopicsConfig.getConsumerGroupBatch(), e.getMessage(), e);
        }
    }
}
