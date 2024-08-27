create table order_event
(
    id                  varchar(50) not null
        constraint order_event_pk
            primary key,
    event_type          varchar(100),
    correlation_id      varchar(50),
    timestamp           timestamp,
    creator_app         varchar(100),
    creator_app_version varchar(100),
    environment         varchar(100),
    order_id            varchar(50),
    customer_id         varchar(50),
    total_value         numeric(10, 2),
    date                timestamp,
    payment_method      varchar(50),
    created_at          timestamp,
    updated_at          timestamp
);

create table product_event
(
    id             varchar(50) not null
        constraint product_event_pk
            primary key,
    order_event_id varchar(50) not null,
    product_id     varchar(50),
    category_id    varchar(50),
    name           varchar(500),
    price          numeric(10, 2),
    created_at     timestamp,
    updated_at     timestamp,
    constraint fk_order_event foreign key (order_event_id) references order_event (id)
);