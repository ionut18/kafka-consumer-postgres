package ro.poc.kafkaconsumerpostgres.messaging;

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

import java.util.List;
import java.util.Map;

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
    public void consume(KafkaEvent<DocumentModel> record) {
        log.info("Received record: {}", record.getPayload());
        BindingResult result = new BeanPropertyBindingResult(record.getPayload(), "record");
        validator.validate(record, result);
        if (result.hasErrors()) {
            log.error("Validation errors: {}", result.getAllErrors());
        }
//        log.info("[{}] Consuming message {} ", groupId, record.value().toString());
//        try {
//            documentService.save(record.key(), record.value());
//        } catch (Exception e) {
//            log.error("[{}] Error consuming message {}", groupId, e.getMessage(), e);
//        }
//        log.info("[{}] Finished consuming message {} ", groupId, record.value().toString());
    }

//    @KafkaListener(topics = "#{kafkaTopicsConfig.getDocumentsTopic()}",
//            groupId = "#{kafkaTopicsConfig.getConsumerGroupBatch()}",
//            containerFactory = "kafkaDocumentBatchListenerContainerFactory")
//    public void consumeBatch(final List<ConsumerRecord<String, KafkaEvent<DocumentModel>>> records) {
//        log.info("[{}] Consuming {} messages", kafkaTopicsConfig.getConsumerGroupBatch(), records.size());
//        try {
//            records.forEach(record -> log.info(record.value().toString()));
//            documentService.saveAll(records);
//        } catch (Exception e) {
//            log.error("[{}] Error consuming message {}", kafkaTopicsConfig.getConsumerGroupBatch(), e.getMessage(), e);
//        }
//        log.info("[{}] Finished consuming {} messages", kafkaTopicsConfig.getConsumerGroupBatch(), records.size());
//    }
}
