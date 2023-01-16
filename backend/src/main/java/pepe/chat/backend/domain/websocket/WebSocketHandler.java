package pepe.chat.backend.domain.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pepe.chat.backend.domain.auth.service.AuthService;
import pepe.chat.backend.domain.websocket.model.Message;
import pepe.chat.backend.domain.websocket.model.Session;

import java.io.IOException;
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

    @Autowired
    public WebSocketHandler(AuthService service) {
        this.service = service;
    }

    @Override
    public void handleTextMessage(WebSocketSession session,
                                  TextMessage rawMessage) throws IOException {
        var om = new ObjectMapper();
        try {
            var message =
                    om.readValue(rawMessage.getPayload(), Message.class);
            var result = handleMessages(session, message);
            if (result.isEmpty())
                return;
            session.sendMessage(result.get().toMessage());
        } catch (JsonProcessingException ignored) {
        }
        var message = Message.builder().type("faulty-message").build();
        session.sendMessage(message.toMessage());
    }


    private Optional<Message<?>> handleMessages(WebSocketSession session,
                                                Message<?> message) {
        try {
            switch (message.getType()) {
                case "login" -> {
                    var content = safeCast(message.getBody(), String.class);
                    if (content.isEmpty()) {
                        return Optional.of(Message.builder().type("no-token")
                                .build());
                    }
                    var result = handleLogin(session, content.get());
                    return result
                            .map(s -> Message.builder().type(s).build());
                }
                case "message" -> {

                }
                case "create-channel" -> {

                }

                default -> {
                    return Optional.of(
                            Message.builder()
                            .type("unknown-message")
                            .build()
                    );
                }
            }

        } catch (Exception ignored) {

        }
        return Optional.of(Message.builder().type("invalid-message").build());
    }

    private void broadcastToUsers(Message<?> message) throws JsonProcessingException {
        sendMessage(message, sessions.values());
    }

    private void sendMessage(Message<?> message,
                             Collection<Session> sessions) throws JsonProcessingException {
        for (var session : sessions) {
            try {
                session.getSession().sendMessage(message.toMessage());
            } catch (JsonProcessingException je) {
                throw je;
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

    private void broadcastToUsersExcept(Message<?> message,
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
