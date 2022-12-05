package pepe.chat.backend.domain.channel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pepe.chat.backend.domain.channel.model.Channel;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, UUID> {

    List<Channel> getAllBy();
}
