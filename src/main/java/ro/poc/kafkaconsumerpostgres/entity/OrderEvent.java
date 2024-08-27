package ro.poc.kafkaconsumerpostgres.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String eventType;
    private String correlationId;
    private LocalDateTime timestamp;
    private String creatorApp;
    private String creatorAppVersion;
    private String environment;

    private String orderId;
    private String customerId;
    private BigDecimal totalValue;
    private LocalDateTime date;
    private String paymentMethod;

    @OneToMany(mappedBy = "orderEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ProductEvent> productEvents = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void addProductEvent(final ProductEvent productEvent) {
        productEvents.add(productEvent);
        productEvent.setOrderEvent(this);
    }

    public void removeProductEvent(final ProductEvent productEvent) {
        productEvents.remove(productEvent);
        productEvent.setOrderEvent(null);
    }
}
