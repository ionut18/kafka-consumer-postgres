package ro.poc.kafkaconsumerpostgres.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.poc.kafkaconsumerpostgres.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, String> {
}
