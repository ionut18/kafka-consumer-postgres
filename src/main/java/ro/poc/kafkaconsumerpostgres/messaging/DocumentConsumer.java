package ro.poc.kafkaconsumerpostgres.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import ro.poc.kafkaconsumerpostgres.config.KafkaTopicsConfig;
import ro.poc.kafkaconsumerpostgres.model.DocumentModel;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;
import ro.poc.kafkaconsumerpostgres.service.DocumentService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentConsumer {

    private final DocumentService documentService;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    @KafkaListener(topics = "#{kafkaTopicsConfig.getDocumentsTopic()}",
            containerFactory = "kafkaDocumentListenerContainerFactory")
    public void consume(final ConsumerRecord<String, KafkaEvent<DocumentModel>> record,
                        @Header(KafkaHeaders.GROUP_ID) final String groupId) {
        log.info("[{}] Consuming message {} ", groupId, record.value().toString());
        try {
            documentService.save(record.key(), record.value());
        } catch (Exception e) {
            log.error("[{}] Error consuming message {}", groupId, e.getMessage(), e);
        }
        log.info("[{}] Finished consuming message {} ", groupId, record.value().toString());
    }

    @KafkaListener(topics = "#{kafkaTopicsConfig.getDocumentsTopic()}",
            groupId = "#{kafkaTopicsConfig.getConsumerGroupBatch()}",
            containerFactory = "kafkaDocumentBatchListenerContainerFactory")
    public void consumeBatch(final List<ConsumerRecord<String, KafkaEvent<DocumentModel>>> records) {
        log.info("[{}] Consuming {} messages", kafkaTopicsConfig.getConsumerGroupBatch(), records.size());
        try {
            records.forEach(record -> log.info(record.value().toString()));
            documentService.saveAll(records);
        } catch (Exception e) {
            log.error("[{}] Error consuming message {}", kafkaTopicsConfig.getConsumerGroupBatch(), e.getMessage(), e);
        }
        log.info("[{}] Finished consuming {} messages", kafkaTopicsConfig.getConsumerGroupBatch(), records.size());
    }
}
