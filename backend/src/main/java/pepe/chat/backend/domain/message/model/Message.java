package pepe.chat.backend.domain.message.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import pepe.chat.backend.domain.channel.model.Channel;
import pepe.chat.backend.domain.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    @Column(length = 500)
    private String message;
    @ManyToOne
    private User user;
    @ManyToOne
    private Channel channel;
    @CreationTimestamp
    private LocalDateTime created;
}
