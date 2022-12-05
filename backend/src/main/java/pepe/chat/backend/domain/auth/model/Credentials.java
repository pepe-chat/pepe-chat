package pepe.chat.backend.domain.auth.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Credentials {
    private String username;
    private String password;
}
