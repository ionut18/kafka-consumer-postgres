package ro.poc.kafkaconsumerpostgres.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.poc.kafkaconsumerpostgres.entity.DocumentEvent;

public interface DocumentEventRepository extends JpaRepository<DocumentEvent, String> {
}
