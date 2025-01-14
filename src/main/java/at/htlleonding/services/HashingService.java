package at.htlleonding.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Slf4j
@ApplicationScoped
public class HashingService {
    @ConfigProperty(name = "WMC_QUARKUS_PASSWORDS_HASH_PEPPER")
    String pepper;

    private static final int COST = 13;

    public String hashPassword(@NonNull String password) {
        return BCrypt.withDefaults().hashToString(COST, (password + pepper).toCharArray());
    }

    public boolean comparePasswords(@NonNull String rawPassword, @NonNull String hashedPassword) {
        return BCrypt.verifyer().verify(
                (rawPassword + pepper).toCharArray(),
                hashedPassword
        ).verified;
    }
}
