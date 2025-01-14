package at.htlleonding.services;

import at.htlleonding.models.User;
import at.htlleonding.repositories.UsersRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class SignService {
    private final UsersRepository usersRepository;
    private final HashingService hashingService;
    private final JwtService jwtService;

    public SignService(
            UsersRepository usersRepository,
            HashingService hashingService,
            JwtService jwtService
    ) {
        this.usersRepository = usersRepository;
        this.hashingService = hashingService;
        this.jwtService = jwtService;
    }

    public Optional<String> login(@NonNull String email, @NonNull String rawPassword) {
        Optional<User> optionalUser = usersRepository
                .find("email", email)
                .firstResultOptional();

        if (optionalUser.isEmpty()) {
            log.info("User not found");
            return Optional.empty();
        }

        User user = optionalUser.get();
        boolean isPasswordCorrect = hashingService.comparePasswords(rawPassword, user.getPasswordHash());

        if (!isPasswordCorrect) {
            log.info("Password does not match");
            return Optional.empty();
        }

        return Optional.of(jwtService.generateToken(
                user.getEmail()
        ));
    }

    public Optional<String> signUp(@NonNull String email, @NonNull String phoneNumber, @NonNull String rawPassword) {
        Optional<User> optionalUser = usersRepository
                .find("email", email)
                .firstResultOptional();

        if (optionalUser.isPresent()) {
            log.info("User already exists");
            return Optional.empty();
        }

        try {
            var hash = this.hashingService.hashPassword(rawPassword);
            var user = User.builder().email(email).passwordHash(hash).phoneNumber(phoneNumber).build();
            usersRepository.persist(user);
        } catch (Exception e) {
            log.warn(e.getMessage());
            log.warn(String.join("\n", Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()));
            log.info("Could not add user to database");
            return Optional.empty();
        }

        return Optional.of(jwtService.generateToken(
                email
        ));
    }
}
