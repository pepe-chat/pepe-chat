package pepe.chat.backend.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pepe.chat.backend.domain.user.model.UserDTO;
import pepe.chat.backend.domain.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository repository;

    public List<UserDTO> getUsers() {
        return this.repository.getUsers()
                .stream()
                .map(UserDTO::new)
                .toList();
    }
}
