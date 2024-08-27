package ro.poc.kafkaconsumerpostgres.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.poc.kafkaconsumerpostgres.entity.OrderEvent;

@Repository
public interface OrderEventRepository extends JpaRepository<OrderEvent, String> {
}
