package pepe.chat.backend.domain.websocket.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.socket.TextMessage;

@Data
@Builder
@JsonSerialize
@JsonDeserialize
public class Message<T> {
    private String type;
    private T body;

    private final static ObjectMapper om = new ObjectMapper();

    public TextMessage toMessage() throws JsonProcessingException {
        return new TextMessage(om.writeValueAsString(this));
    }
}