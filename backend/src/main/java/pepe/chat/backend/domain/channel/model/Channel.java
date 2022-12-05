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
    @GeneratedValue
    private UUID channel;

    @Column
    private String name;

    @OneToMany
    private List<Message> messages;
}
