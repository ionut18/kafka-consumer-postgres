package ro.poc.kafkaconsumerpostgres.mapper;

import ro.poc.kafkaconsumerpostgres.entity.Order;
import ro.poc.kafkaconsumerpostgres.entity.OrderEvent;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;
import ro.poc.kafkaconsumerpostgres.model.MetaModel;
import ro.poc.kafkaconsumerpostgres.model.OrderModel;

public class OrderMapper {

    public static Order toOrder(final OrderModel model) {
        final Order order = Order.builder()
                .id(model.getId())
                .customerId(model.getCustomerId())
                .totalValue(model.getTotalValue())
                .date(model.getDate())
                .paymentMethod(model.getPaymentMethod())
                .build();
        model.getProducts().stream()
                .map(ProductMapper::toProduct)
                .forEach(order::addProduct);
        return order;
    }

    public static OrderEvent toOrderEvent(final KafkaEvent<OrderModel> event) {
        final MetaModel meta = event.getMeta();
        final OrderModel model = event.getPayload();
        final OrderEvent orderEvent = OrderEvent.builder()
                .eventType(meta.getEventType())
                .correlationId(meta.getCorrelationId())
                .timestamp(meta.getTimestamp())
                .creatorApp(meta.getCreatorApp())
                .creatorAppVersion(meta.getCreatorAppVersion())
                .environment(meta.getEnvironment())
                .orderId(model.getId())
                .customerId(model.getCustomerId())
                .totalValue(model.getTotalValue())
                .date(model.getDate())
                .paymentMethod(model.getPaymentMethod())
                .build();
        model.getProducts().stream()
                .map(ProductMapper::toProductEvent)
                .forEach(orderEvent::addProductEvent);
        return orderEvent;
    }
}
