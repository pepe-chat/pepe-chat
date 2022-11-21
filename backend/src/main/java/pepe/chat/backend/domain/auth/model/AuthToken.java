package pepe.chat.backend.domain.auth.model;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import pepe.chat.backend.domain.user.model.User;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@NoArgsConstructor
public class AuthToken {
    private UUID user;
    private LocalDateTime until;
    @Value("application.secret")
    private String secret;

    public AuthToken(User user) {
        this.user = user.getUuid();
        this.until = LocalDateTime.now().plusHours(24);
    }

    public String build() {
        var info = String.format("%s;%d", user.toString(),
                until.toInstant(ZoneOffset.UTC).getEpochSecond());

        try {
            var cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(secret.getBytes(), "AES"));

            return new String(cipher.doFinal(info.getBytes()));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                 InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}
