package pepe.chat.backend.domain.channel.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class ChannelDTO {
    private UUID channel;
    private String name;
}
