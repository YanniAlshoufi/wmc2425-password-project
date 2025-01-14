package at.htlleonding.services;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class JwtService {
    public String generateToken(@NonNull String username) {
        return Jwt.issuer("http://localhost")
                .upn(username)
                .expiresIn(1000 * 60 * 15) // 15 minutes
                .sign();
    }
}
