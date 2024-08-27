package ro.poc.kafkaconsumerpostgres.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String eventType;
    private String correlationId;
    private LocalDateTime timestamp;
    private String creatorApp;
    private String creatorAppVersion;
    private String environment;

    private String documentId;
    private String title;
    private String description;
    private String author;
    private String content;
    private Integer pages;
    private LocalDateTime sentDate;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
