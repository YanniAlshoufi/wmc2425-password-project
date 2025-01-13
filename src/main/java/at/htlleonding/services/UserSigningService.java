package at.htlleonding.services;

import at.htlleonding.models.User;
import at.htlleonding.repositories.UsersRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class UserSigningService {
    private final UsersRepository usersRepository;
    private final HashingService hashingService;

    public UserSigningService(UsersRepository usersRepository, HashingService hashingService) {
        this.usersRepository = usersRepository;
        this.hashingService = hashingService;
    }

    public Optional<String> login(String email, String password) {
        Optional<User> optionalUser = usersRepository.find("User user where user.email = :email", email).firstResultOptional();
        if (optionalUser.isEmpty()) return Optional.empty();
        User user = optionalUser.get();
        boolean isPasswordCorrect = hashingService.comparePasswords(password, user.getPasswordHash());
        if (!isPasswordCorrect) return Optional.empty();
        String token = Jwt.issuer("https://example.com/issuer")  // TODO: not sure if this matters, idk what URL we'd have if yes since this isn't an actual thing we're launching
                .upn(user.getEmail())
                .groups("User")
                .claim("mi-ku", "39")  // TODO: I don't understand what the claim thing is for/why it's needed, docs don't seem to explain it properly and put birthdate of the user there
                .sign();
         return Optional.of(token);
    }
}
