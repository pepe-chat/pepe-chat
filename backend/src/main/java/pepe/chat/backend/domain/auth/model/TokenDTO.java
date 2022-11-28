package pepe.chat.backend.domain.auth.model;

import lombok.*;

@Data
@AllArgsConstructor
public class TokenDTO {
    private String token;
    private long validUntil;
}
