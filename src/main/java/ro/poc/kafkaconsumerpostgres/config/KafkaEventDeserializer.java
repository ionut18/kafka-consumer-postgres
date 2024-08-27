package ro.poc.kafkaconsumerpostgres.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;

@RequiredArgsConstructor
public class KafkaEventDeserializer<T> implements Deserializer<KafkaEvent<T>> {

    private final TypeReference<KafkaEvent<T>> typeReference;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public KafkaEvent<T> deserialize(String topic, byte[] data) {
        try {
            objectMapper.registerModules(new JavaTimeModule());
            return objectMapper.readValue(data, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
