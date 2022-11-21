package pepe.chat.backend.domain.channel.model;

import lombok.Data;
import pepe.chat.backend.domain.message.model.Message;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID channel;
    @OneToMany
    private List<Message> messages;
}
