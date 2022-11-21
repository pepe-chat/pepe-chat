package pepe.chat.backend.domain.user.model;

import lombok.Data;
import pepe.chat.backend.domain.message.model.Message;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private UUID uuid;
    private boolean online;
    @Column(unique = true)
    private String username;
    private String password;
    @OneToMany
    private List<Message> messages;

}
