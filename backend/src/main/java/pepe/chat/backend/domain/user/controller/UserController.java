package pepe.chat.backend.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pepe.chat.backend.domain.auth.AuthException;
import pepe.chat.backend.domain.auth.service.AuthService;
import pepe.chat.backend.domain.user.model.UserDTO;
import pepe.chat.backend.domain.user.service.UserService;

import java.util.List;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private UserService service;
    private AuthService authService;


    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(this.service.getUsers());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@RequestHeader("Authorization") String auth) {
        try {
            var user = this.authService.getUserFromToken(auth);
            return ResponseEntity.ok(user);
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }

    }
}
