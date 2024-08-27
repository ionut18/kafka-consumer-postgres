package ro.poc.kafkaconsumerpostgres.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.poc.kafkaconsumerpostgres.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
}
