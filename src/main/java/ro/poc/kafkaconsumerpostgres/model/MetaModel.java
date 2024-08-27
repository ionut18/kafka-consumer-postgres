package ro.poc.kafkaconsumerpostgres.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetaModel {
    private String eventType;
    private String correlationId;
    private LocalDateTime timestamp;
    private String creatorApp;
    private String creatorAppVersion;
    private String environment;
}
