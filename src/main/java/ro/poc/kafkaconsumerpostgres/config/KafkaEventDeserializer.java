package ro.poc.kafkaconsumerpostgres.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.kafka.support.serializer.DeserializationException;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;

public class KafkaEventDeserializer<T> implements Deserializer<KafkaEvent<T>> {

    private final TypeReference<KafkaEvent<T>> typeReference;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaEventDeserializer(final TypeReference<KafkaEvent<T>> typeReference) {
        this.typeReference = typeReference;
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public KafkaEvent<T> deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, typeReference);
        } catch (Exception e) {
            throw new DeserializationException("Error deserializing message", data, false, e);
        }
    }
}
