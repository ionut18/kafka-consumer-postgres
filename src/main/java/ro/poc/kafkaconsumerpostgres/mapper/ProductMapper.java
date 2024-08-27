package ro.poc.kafkaconsumerpostgres.mapper;

import ro.poc.kafkaconsumerpostgres.entity.Product;
import ro.poc.kafkaconsumerpostgres.entity.ProductEvent;
import ro.poc.kafkaconsumerpostgres.model.ProductModel;

public class ProductMapper {

    public static Product toProduct(final ProductModel model) {
        return Product.builder()
                .id(model.getId())
                .name(model.getName())
                .price(model.getPrice())
                .categoryId(model.getCategoryId())
                .build();
    }

    public static ProductEvent toProductEvent(final ProductModel model) {
        return ProductEvent.builder()
                .productId(model.getId())
                .name(model.getName())
                .price(model.getPrice())
                .categoryId(model.getCategoryId())
                .build();
    }
}
