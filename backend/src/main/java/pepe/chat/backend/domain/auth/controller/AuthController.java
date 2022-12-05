package pepe.chat.backend.domain.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pepe.chat.backend.domain.auth.AuthException;
import pepe.chat.backend.domain.auth.model.Credentials;
import pepe.chat.backend.domain.auth.model.TokenDTO;
import pepe.chat.backend.domain.auth.service.AuthService;

@RestController
public class AuthController {
    private final AuthService service;

    @Autowired
    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Credentials credentials) {
        try {
            return ResponseEntity.ok(service.login(credentials.getUsername(),
                    credentials.getPassword()));
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Credentials credentials) {
        try {
            return ResponseEntity.status(201)
                    .body(service.createUser(credentials.getUsername(),
                            credentials.getPassword()));
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
