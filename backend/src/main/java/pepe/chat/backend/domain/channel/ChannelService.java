package pepe.chat.backend.domain.channel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pepe.chat.backend.domain.channel.model.Channel;
import pepe.chat.backend.domain.channel.model.ChannelDTO;
import pepe.chat.backend.domain.channel.repository.ChannelRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository repository;

    public List<ChannelDTO> getChannels() {
        return this.repository.getAllBy()
                .stream()
                .map((channel) -> new ChannelDTO().setName(channel.getName()))
                .toList();
    }

    public Optional<Channel> getChannelByUUID(UUID id) {
        return repository.findById(id);
    }
}
