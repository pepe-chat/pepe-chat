package pepe.chat.backend.domain.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pepe.chat.backend.domain.message.model.Message;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
}
