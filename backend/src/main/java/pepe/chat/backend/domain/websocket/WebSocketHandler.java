package pepe.chat.backend.domain.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pepe.chat.backend.domain.auth.service.AuthService;
import pepe.chat.backend.domain.channel.ChannelService;
import pepe.chat.backend.domain.message.model.Message;
import pepe.chat.backend.domain.message.repository.MessageRepository;
import pepe.chat.backend.domain.user.service.UserService;
import pepe.chat.backend.domain.websocket.model.Session;
import pepe.chat.backend.domain.websocket.model.WebSocketMessage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final ConcurrentHashMap<String, Session> sessions =
            new ConcurrentHashMap<>();

    private final Logger logger =
            LoggerFactory.getLogger(WebSocketHandler.class);
    private final AuthService service;
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;

    @Autowired
    public WebSocketHandler(AuthService service, MessageRepository messageRepository, UserService userService, ChannelService channelService) {
        this.service = service;
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public void handleTextMessage(WebSocketSession session,
                                  TextMessage rawMessage) throws IOException {
        var om = new ObjectMapper();
        try {
            var message =
                    om.readValue(rawMessage.getPayload(), WebSocketMessage.class);
            var result = handleMessages(session, message);
            if (result.isEmpty())
                return;
            session.sendMessage(result.get().toMessage());
        } catch (JsonProcessingException ignored) {
        }
        var message = WebSocketMessage.builder().type("faulty-message").build();
        session.sendMessage(message.toMessage());
    }


    private Optional<WebSocketMessage<?>> handleMessages(WebSocketSession session,
                                                         WebSocketMessage<?> message) {
        try {
            switch (message.getType()) {
                case "login" -> {
                    var content = safeCast(message.getBody(), String.class);
                    if (content.isEmpty()) {
                        return Optional.of(WebSocketMessage.builder().type("no-token")
                                .build());
                    }
                    var result = handleLogin(session, content.get());
                    return result
                            .map(s -> WebSocketMessage.builder().type(s).build());
                }
                case "message" -> {
                    var content = safeCast(message.getBody(), String.class);
                    if (content.isEmpty()) {
                        return Optional.of(WebSocketMessage.builder().type("no-message").build());
                    }
                }
                case "create-channel" -> {

                }

                default -> {
                    return Optional.of(
                            WebSocketMessage.builder()
                                    .type("unknown-message")
                                    .build()
                    );
                }
            }

        } catch (Exception ignored) {

        }
        return Optional.of(WebSocketMessage.builder().type("invalid-message").build());
    }


    @Data
    static class NewMessage {
        private String message;
        private UUID channel;
    }

    private void userSendsMessage(Session session, NewMessage message) {
        var user = userService.getUserFromUUID(session.getUser());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("user does not exist");
        }

        var channel = channelService.getChannelByUUID(message.getChannel());
        if (channel.isEmpty()) {
            throw new IllegalArgumentException("channel does not exist");
        }

        var newMessage = Message.builder()
                .message(message.getMessage())
                .user(user.get())
                .created(LocalDateTime.now())
                .channel(channel.get()).build();
    }

    private void broadcastToUsers(WebSocketMessage<?> message) {
        sendMessage(message, sessions.values());
    }

    private void sendMessage(WebSocketMessage<?> message,
                             Collection<Session> sessions) {
        for (var session : sessions) {
            try {
                session.getSession().sendMessage(message.toMessage());
            } catch (JsonProcessingException je) {
                throw new RuntimeException(je);
            } catch (IOException e) {
                try {
                    this.sessions.remove(session.getSession().getId());
                    session.getSession().close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private void broadcastToUsersExcept(WebSocketMessage<?> message,
                                        UUID userToIgnore) throws JsonProcessingException {
        sendMessage(message,
                sessions.values().stream()
                        .filter(val -> !val.getUser().equals(userToIgnore))
                        .toList());
    }

    private <T> Optional<T> safeCast(Object obj, Class<T> clazz) {
        if (clazz.isInstance(obj))
            return Optional.of(clazz.cast(obj));
        return Optional.empty();
    }

    private Optional<String> handleLogin(WebSocketSession session,
                                         String content) {
        var token = service.getToken(content);
        if (token.isEmpty() || service.isValidToken(token.get())) {
            return Optional.of("invalid-token");
        }

        var authToken = token.get();

        sessions.put(session.getId(),
                Session
                        .builder()
                        .user(authToken.getUser())
                        .session(session)
                        .build());

        return Optional.empty();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("Connection established to {}",
                Objects.requireNonNull(session.getRemoteAddress()));
    }
}
