package ro.poc.kafkaconsumerpostgres.mapper;

import ro.poc.kafkaconsumerpostgres.entity.Document;
import ro.poc.kafkaconsumerpostgres.entity.DocumentEvent;
import ro.poc.kafkaconsumerpostgres.model.DocumentModel;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;
import ro.poc.kafkaconsumerpostgres.model.MetaModel;

public class DocumentMapper {

    public static Document toDocument(final String key, final DocumentModel model) {
        return Document.builder()
                .id(key)
                .title(model.getTitle())
                .content(model.getContent())
                .pages(model.getPages())
                .description(model.getDescription())
                .author(model.getAuthor())
                .sentDate(model.getSentDate())
                .build();
    }

    public static DocumentEvent toDocumentEvent(final String key, final KafkaEvent<DocumentModel> event) {
        final MetaModel meta = event.getMeta();
        final DocumentModel model = event.getPayload();
        return DocumentEvent.builder()
                .eventType(meta.getEventType())
                .correlationId(meta.getCorrelationId())
                .timestamp(meta.getTimestamp())
                .creatorApp(meta.getCreatorApp())
                .creatorAppVersion(meta.getCreatorAppVersion())
                .environment(meta.getEnvironment())
                .documentId(key)
                .title(model.getTitle())
                .content(model.getContent())
                .pages(model.getPages())
                .description(model.getDescription())
                .author(model.getAuthor())
                .sentDate(model.getSentDate())
                .build();
    }
}
