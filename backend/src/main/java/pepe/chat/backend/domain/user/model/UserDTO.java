package pepe.chat.backend.domain.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
@JsonDeserialize
@JsonSerialize
@JsonIgnoreProperties(value = {"online"}, allowGetters = true)
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

