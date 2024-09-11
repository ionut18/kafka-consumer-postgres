package ro.poc.kafkaconsumerpostgres.model;

import jakarta.validation.Valid;
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
    @Valid
    @NotNull(message = "Payload can't be null")
    private T payload;
}
