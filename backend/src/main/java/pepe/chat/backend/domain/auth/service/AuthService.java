package pepe.chat.backend.domain.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pepe.chat.backend.domain.auth.AuthException;
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

    public TokenDTO login(String username, String password) throws AuthException {
        var opt = repository.findByUsername(username);
        var user = opt.orElseThrow(AuthException::noUser);
        if (!user.getPassword().equals(password)) {
            throw AuthException.noUser();
        }

        return new AuthToken(user, false, properties.getSecret()).intoDTO();
    }

    public TokenDTO createUser(String username, String password) throws AuthException {
        if (repository.existsByUsername(username)) {
            throw AuthException.usernameTaken();
        }

        var user = User.builder()
                .online(true)
                .password(password)
                .username(username)
                .build();

        user = repository.save(user);

        return new AuthToken(user, false, properties.getSecret()).intoDTO();
    }

    public User getUserFromToken(String token) throws AuthException {
        try {
            var user = repository.findById(new AuthToken(token).getUser());

            return user.orElseThrow(AuthException::noUser);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("invalid-token");
        }
    }

    public Optional<AuthToken> getToken(String token) {
        try {
            return Optional.of(new AuthToken(token));
        } catch (AuthException ignored) {
        }

        return Optional.empty();
    }

    public boolean isValidToken(AuthToken token) {
        return token.getUntil().isAfter(LocalDateTime.now()) &&
                repository.existsById(token.getUser());
    }
}
