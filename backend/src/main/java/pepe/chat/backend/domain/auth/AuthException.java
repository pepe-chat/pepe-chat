package pepe.chat.backend.domain.auth;

public class AuthException extends Exception {
    private AuthException(String s) {
        super(s);
    }

    public static AuthException noUser() {
        return new AuthException("no-user");
    }

    public static AuthException invalidToken() {
        return new AuthException("invalid-token");
    }

    public static AuthException usernameTaken() {
        return new AuthException("username-taken");
    }

    public static AuthException expiredToken() {
        return new AuthException("expired-token");
    }
}
