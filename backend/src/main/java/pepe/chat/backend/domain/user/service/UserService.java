package pepe.chat.backend.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pepe.chat.backend.domain.user.model.User;
import pepe.chat.backend.domain.user.model.UserDTO;
import pepe.chat.backend.domain.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<UserDTO> getUsers() {
        return this.repository.findAll()
                .stream()
                .map(UserDTO::new)
                .toList();
    }

    public Optional<User> getUserFromUUID(UUID user) {
        return repository.findById(user);
    }
}
