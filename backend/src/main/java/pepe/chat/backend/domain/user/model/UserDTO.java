package pepe.chat.backend.domain.user.model;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String id;
    private boolean online;

    public UserDTO(User u) {
        this.username = u.getUsername();
        this.id = u.getUuid().toString();
        this.online = u.isOnline();
    }
}

