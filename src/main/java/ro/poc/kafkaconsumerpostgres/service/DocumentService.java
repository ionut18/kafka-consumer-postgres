package ro.poc.kafkaconsumerpostgres.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;
import ro.poc.kafkaconsumerpostgres.entity.Document;
import ro.poc.kafkaconsumerpostgres.entity.DocumentEvent;
import ro.poc.kafkaconsumerpostgres.mapper.DocumentMapper;
import ro.poc.kafkaconsumerpostgres.model.DocumentModel;
import ro.poc.kafkaconsumerpostgres.model.KafkaEvent;
import ro.poc.kafkaconsumerpostgres.repository.DocumentEventRepository;
import ro.poc.kafkaconsumerpostgres.repository.DocumentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentEventRepository documentEventRepository;

    @Transactional
    public void save(final String key, final KafkaEvent<DocumentModel> event) {
        final DocumentEvent documentEvent = DocumentMapper.toDocumentEvent(key, event);
        documentEventRepository.save(documentEvent);
        final Document document = DocumentMapper.toDocument(key, event.getPayload());
        documentRepository.save(document);
    }

    @Transactional
    public void saveAll(final List<ConsumerRecord<String, KafkaEvent<DocumentModel>>> records) {
        final List<DocumentEvent> documentEvents = records.stream()
                .map(record -> DocumentMapper.toDocumentEvent(record.key(), record.value()))
                .collect(Collectors.toList());
        documentEventRepository.saveAll(documentEvents);
        final List<Document> documents= records.stream()
                .map(record -> DocumentMapper.toDocument(record.key() + "batch", record.value().getPayload()))
                .collect(Collectors.toList());
        documentRepository.saveAll(documents);
    }
}
