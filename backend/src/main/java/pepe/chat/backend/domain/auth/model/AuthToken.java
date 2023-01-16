package pepe.chat.backend.domain.auth.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.util.buf.HexUtils;
import pepe.chat.backend.domain.auth.AuthException;
import pepe.chat.backend.domain.user.model.User;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class AuthToken {
    private UUID user;
    private LocalDateTime until;
    private byte[] secret;

    private final static String KEY_ALGORITHM = "AES";
    private final static String ENCODING_ALGORITHM = "AES";


    private boolean isRefresh;

    public AuthToken(User user, boolean isRefresh, String secret) {
        this.user = user.getUuid();
        this.until = LocalDateTime.now().plusHours(24);
        this.isRefresh = isRefresh;
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            this.secret = digest.digest(secret.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    public TokenDTO intoDTO() {
        return new TokenDTO(this.build(),
                this.until.toEpochSecond(ZoneOffset.UTC));
    }

    public AuthToken(String token) throws AuthException {
        try {
            var cipher = Cipher.getInstance(ENCODING_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret, KEY_ALGORITHM));
            var encToken =
                    new String(cipher.doFinal(HexUtils.fromHexString(token)));
            if (encToken.matches(".+?;\\d+?;\\d")) {
                var info = encToken.split(";");
                this.user = UUID.fromString(info[0]);
                this.until =
                        LocalDateTime.ofEpochSecond(Long.parseLong(info[1]),
                                0, ZoneOffset.UTC);
                this.isRefresh = info[2].equals("1");
            } else {
                throw AuthException.invalidToken();
            }

            if (this.getUntil().isBefore(LocalDateTime.now())) {
                throw AuthException.expiredToken();
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException |
                 IllegalBlockSizeException | NoSuchPaddingException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public String build() {
        var info = String.format("%s;%d;%d", user.toString(),
                until.toInstant(ZoneOffset.UTC).getEpochSecond(), isRefresh ?
                        1 : 0);

        try {
            var cipher = Cipher.getInstance(ENCODING_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secret, KEY_ALGORITHM));

            return HexUtils.toHexString(cipher.doFinal(info.getBytes()));
        } catch (NoSuchAlgorithmException | InvalidKeyException |
                 NoSuchPaddingException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}
