package ro.poc.kafkaconsumerpostgres.config;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.DeserializationException;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;

@Slf4j
public class KafkaEventErrorHandler implements CommonErrorHandler {

    @Override
    public boolean handleOne(final Exception exception,
                             final ConsumerRecord<?, ?> record,
                             final Consumer<?, ?> consumer,
                             final MessageListenerContainer container) {
        handleError(exception, record, consumer);
        return true;
    }

    private static void handleError(final Exception exception,
                                    final ConsumerRecord<?, ?> record,
                                    final Consumer<?, ?> consumer) {
        if (exception.getCause() instanceof DeserializationException) {
            log.error("DeserializationException error on topic {} with id {}", record.topic(), record.key(), exception);
            final TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
            final long nextOffset = record.offset() + 1L;
            consumer.seek(topicPartition, nextOffset);
            consumer.commitSync();
        } else {
            log.error("Error on topic {}", record.topic(), exception);
            throw new RuntimeException(exception);
        }
    }

    //todo: not working
    @Override
    public void handleBatch(final Exception exception,
                            final ConsumerRecords<?, ?> records,
                            final Consumer<?, ?> consumer,
                            final MessageListenerContainer container,
                            final Runnable invokeListener) {
        if (exception.getCause() instanceof DeserializationException) {
            log.error("DeserializationException error on batch processing", exception);
            for (final ConsumerRecord<?, ?> record : records) {
                log.error("record {}", record);
                try {
                    KafkaEventDeserializer<KafkaEvent<?>> deserializer =  new KafkaEventDeserializer<>(new TypeReference<>() {});
                    deserializer.deserialize(record.topic(), record.value() != null ? (byte[]) record.value() : null);
                } catch (DeserializationException e) {
                    log.error("DeserializationException error batch on topic {} on record with id {}", record.topic(), record.key(), e);
                    TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
                    long nextOffset = record.offset() + 1;
                    consumer.seek(topicPartition, nextOffset);
                }
            }
            consumer.commitSync();
        } else {
            log.error("Error on batch processing", exception);
            throw new RuntimeException(exception);
        }
    }

    @Override
    public boolean isAckAfterHandle() {
        return false;
    }
}