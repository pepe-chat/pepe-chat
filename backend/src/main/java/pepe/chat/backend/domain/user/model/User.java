package pepe.chat.backend.domain.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pepe.chat.backend.domain.message.model.Message;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {
    @GeneratedValue
    @Id
    private UUID uuid;
    private boolean online;
    @Column(unique = true)
    private String username;
    private String password;
    @OneToMany
    private List<Message> messages;

}
