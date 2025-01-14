package at.htlleonding.services;

import at.htlleonding.models.User;
import at.htlleonding.repositories.UsersRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class UserSigningService {
    private final UsersRepository usersRepository;
    private final HashingService hashingService;

    public UserSigningService(UsersRepository usersRepository, HashingService hashingService) {
        this.usersRepository = usersRepository;
        this.hashingService = hashingService;
    }

    public Optional<String> login(String email, String rawPassword) {
        Optional<User> optionalUser = usersRepository
                .find("email", email)
                .firstResultOptional();

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User user = optionalUser.get();
        boolean isPasswordCorrect = hashingService.comparePasswords(rawPassword, user.getPasswordHash());

        if (!isPasswordCorrect) {
            return Optional.empty();
        }

        String token = Jwt.issuer("http://localhost")
                .upn(user.getEmail())
                .expiresIn(1000 * 60 * 15) // 15 minutes
                k.sign();

        return Optional.of(token);
    }

    public Optional<String> signUp(String email, String phoneNumber, String rawPassword) {
        Optional<User> optionalUser = usersRepository
                .find("email", email)
                .firstResultOptional();

        if (optionalUser.isPresent()) {
            return Optional.empty();
        }


        try {
            var hash = this.hashingService.hashPassword(rawPassword);
            var user = User.builder().email(email).passwordHash(hash).phoneNumber(phoneNumber).build();
            usersRepository.persist(user);
        } catch (Exception e) {
            log.warn(e.getMessage());
            log.warn(String.join("\n", Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()));
            return Optional.empty();
        }

        String token = Jwt.issuer("http://localhost")
                .upn(email)
                .expiresIn(1000 * 60 * 15) // 15 minutes
                .sign();

        return Optional.of(token);
    }
}
