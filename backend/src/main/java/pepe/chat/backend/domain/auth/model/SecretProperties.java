package pepe.chat.backend.domain.auth.model;


import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:pepechat.properties")
@Data
public class SecretProperties {
    private String secret;

    @Autowired
    public SecretProperties(@Value("${pepechat.secret}") String secret) {
        this.secret = secret;
    }
}
