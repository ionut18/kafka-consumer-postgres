create table document
(
    id                  varchar(50) not null
        constraint document_pk
            primary key,
    title               text,
    description         text,
    author              text,
    content             text,
    pages               integer,
    sent_date           timestamp,
    created_at          timestamp,
    updated_at          timestamp,
    document_event_type varchar(50)
);

