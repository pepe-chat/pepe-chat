package pepe.chat.backend.domain.websocket.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;
import pepe.chat.backend.domain.user.model.User;

import java.util.UUID;

@Data
@Builder
public class Session {

    private final WebSocketSession session;
    private final UUID user;
}
