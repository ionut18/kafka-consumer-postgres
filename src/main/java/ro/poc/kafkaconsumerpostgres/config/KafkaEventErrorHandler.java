package ro.poc.kafkaconsumerpostgres.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class KafkaEventErrorHandler implements CommonErrorHandler {

    @Override
    public boolean handleOne(Exception thrownException, ConsumerRecord<?, ?> record, Consumer<?, ?> consumer, MessageListenerContainer container) {
        log.error("Got an exception", thrownException);
        if (thrownException.getCause() instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) thrownException.getCause();
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : ex.getBindingResult().getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            // Log or handle the validation errors
            log.error("Validation errors: {}", errors);
        } else {
            // Handle other exceptions
            thrownException.printStackTrace();
        }
        return false;
    }

    @Override
    public void handleRemaining(Exception thrownException, List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer, MessageListenerContainer container) {
        // Handle any other remaining exceptions
        log.error("Handle remaining exceptions", thrownException);
        thrownException.printStackTrace();
    }

    @Override
    public void handleOtherException(Exception thrownException, Consumer<?, ?> consumer, MessageListenerContainer container, boolean batchListener) {
        // Handle other types of exceptions
        log.error("Handle other exceptions", thrownException);
        thrownException.printStackTrace();
    }

    @Override
    public boolean isAckAfterHandle() {
        return true; // Acknowledge the message after handling the error
    }
}