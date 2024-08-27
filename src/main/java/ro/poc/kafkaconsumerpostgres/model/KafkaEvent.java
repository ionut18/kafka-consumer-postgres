package ro.poc.kafkaconsumerpostgres.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaEvent<T> {
    private MetaModel meta;
    private T payload;
}
