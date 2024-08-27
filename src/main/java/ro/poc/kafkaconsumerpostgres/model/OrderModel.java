package ro.poc.kafkaconsumerpostgres.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderModel {
    private String id;
    private String customerId;
    private BigDecimal totalValue;
    private LocalDateTime date;
    private String paymentMethod;
    private List<ProductModel> products;
}
