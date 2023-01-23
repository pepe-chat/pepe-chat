package pepe.chat.backend.domain.message.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonDeserialize
@JsonSerialize
@Data
@Builder
@JsonIgnoreProperties(value = {"user", "created"}, allowGetters = true)
public class MessageDTO {
    private UUID channelId;
    private String content;

    private UUID user;

    private LocalDateTime created;

    public static MessageDTO from(Message message) {
        return MessageDTO.builder()
                .channelId(message.getChannel().getChannel())
                .content(message.getMessage())
                .created(message.getCreated())
                .user(message.getUser().getUuid())
                .build();
    }
}
