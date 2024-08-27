create table orders
(
    id             varchar(50) not null
        constraint orders_pk
            primary key,
    customer_id    varchar(50),
    total_value    numeric(10, 2),
    date           timestamp,
    payment_method varchar(50),
    created_at     timestamp,
    updated_at     timestamp
);

create table product
(
    id          varchar(50) not null
        constraint product_pk
            primary key,
    order_id    varchar(50) not null,
    category_id varchar(50),
    name        varchar(500),
    price       numeric(10, 2),
    created_at  timestamp,
    updated_at  timestamp,
    constraint fk_orders foreign key (order_id) references orders (id)
);