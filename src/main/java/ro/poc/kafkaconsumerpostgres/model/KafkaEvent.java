package ro.poc.kafkaconsumerpostgres.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaEvent<T> {
    @NotNull(message = "Meta can't be null")
    private MetaModel meta;
    @NotNull(message = "Payload can't be null")
    private T payload;
}
