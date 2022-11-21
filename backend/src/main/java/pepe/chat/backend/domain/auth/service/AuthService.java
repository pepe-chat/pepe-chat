package pepe.chat.backend.domain.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pepe.chat.backend.domain.auth.model.AuthToken;
import pepe.chat.backend.domain.user.repository.UserRepository;

import java.util.NoSuchElementException;

@Service
public class AuthService {
    private final UserRepository repository;

    @Autowired
    public AuthService(UserRepository repository) {
        this.repository = repository;
    }

    public String login(String username, String password) {
        var opt = repository.findByUsername(username);
        if (opt.isEmpty()) {
            throw new NoSuchElementException();
        }
        var user = opt.get();
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("password does not match");
        }
        var token = new AuthToken(user);
        return token.build();
    }
}
