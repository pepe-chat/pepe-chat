package pepe.chat.backend.domain.channel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pepe.chat.backend.domain.channel.ChannelService;
import pepe.chat.backend.domain.channel.model.ChannelDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/channels")
public class ChannelController {
    private final ChannelService service;

    @GetMapping("/")
    public ResponseEntity<List<ChannelDTO>> getChannels() {
        return ResponseEntity.ok(this.service.getChannels());
    }
}
