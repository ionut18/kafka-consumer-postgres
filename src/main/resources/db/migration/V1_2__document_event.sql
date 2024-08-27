alter table document
drop
column document_event_type;

create table document_event
(
    id                  varchar(50) not null
        constraint document_event_pk
            primary key,
    event_type          varchar(100),
    correlation_id      varchar(50),
    timestamp           timestamp,
    creator_app         varchar(100),
    creator_app_version varchar(100),
    environment         varchar(100),
    document_id         varchar(50),
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

