package pepe.chat.backend.domain.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pepe.chat.backend.domain.auth.model.AuthToken;
import pepe.chat.backend.domain.auth.model.SecretProperties;
import pepe.chat.backend.domain.auth.model.TokenDTO;
import pepe.chat.backend.domain.user.model.User;
import pepe.chat.backend.domain.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository repository;
    private final SecretProperties properties;

    @Autowired
    public AuthService(UserRepository repository, SecretProperties properties) {
        this.repository = repository;
        this.properties = properties;
    }

    public TokenDTO login(String username, String password) {
        var opt = repository.findByUsername(username);
        var user = opt.orElseThrow(() -> new IllegalArgumentException("user " +
                "does not exist"));
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("password does not match");
        }

        return new AuthToken(user, false, properties.getSecret()).intoDTO();
    }

    public TokenDTO createUser(String username, String password) {
        if (repository.existsByUsername(username)) {
            throw new IllegalArgumentException("username already taken");
        }

        var user = User.builder()
                .online(true)
                .password(password)
                .username(username)
                .build();

        user = repository.save(user);

        return new AuthToken(user, false, properties.getSecret()).intoDTO();
    }

    public Optional<AuthToken> getToken(String token) {
        try {
            return Optional.of(new AuthToken(token));
        } catch (IllegalArgumentException ignored) {
        }

        return Optional.empty();
    }

    public boolean isValidToken(AuthToken token) {
        return token.getUntil().isAfter(LocalDateTime.now()) &&
                repository.existsById(token.getUser());
    }
}
