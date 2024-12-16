package at.htlleonding.users;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.htlleonding.users.dtos.SignInRequest;
import at.htlleonding.users.dtos.SignUpRequest;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class UserSigningService {
    private final String pepper;
    private final UsersRepository repo;

    public UserSigningService(UsersRepository repo, @ConfigProperty(name = "QUARKUS_PASSWORDS_HASH_PEPPER") String pepper) {
        this.pepper = pepper;
        this.repo = repo;
    }

    public boolean signIn(String email, String password) {
        var optionalUser = repo.find("User user where user.email = :email", email).firstResultOptional();

        if (optionalUser.isEmpty()) {
            return false;
        }

        var user = optionalUser.get();
        var passwordWithPepper = (password + this.pepper).toCharArray();

        return BCrypt
                .verifyer()
                .verify(passwordWithPepper, user.getPasswordHash())
                .verified;
    }

    public boolean signUp() {
        throw new RuntimeException("Not yet implemented.");
    }
}
