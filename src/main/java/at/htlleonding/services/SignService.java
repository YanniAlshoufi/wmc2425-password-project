package at.htlleonding.services;

import at.htlleonding.models.User;
import at.htlleonding.repositories.UsersRepository;
import at.htlleonding.services.responses.SignIn;
import at.htlleonding.services.responses.SignUp;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.regex.Pattern;

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

    public SignIn.Response signIn(@NonNull String email, @NonNull String rawPassword) {
        Optional<User> optionalUser = usersRepository
                .find("email", email)
                .firstResultOptional();

        if (optionalUser.isEmpty()) {
            return new SignIn.UserNotFound();
        }

        User user = optionalUser.get();
        boolean isPasswordCorrect = hashingService.comparePasswords(rawPassword, user.getPasswordHash());

        if (!isPasswordCorrect) {
            return new SignIn.IncorrectPassword();
        }

        return new SignIn.Success(jwtService.generateToken(
                user.getEmail()
        ));
    }

    public SignUp.Response signUp(@NonNull String email, @NonNull String phoneNumber, @NonNull String rawPassword) {
        if (!Helpers.isPasswordValid(rawPassword)) {
            return new SignUp.InvalidPassword(
                Helpers.PASSWORD_INVALID_MESSAGE
            );
        }

        Optional<User> optionalUser = usersRepository
                .find("email", email)
                .firstResultOptional();

        if (optionalUser.isPresent()) {
            return new SignUp.UserAlreadyExists();
        }

        var hash = this.hashingService.hash(rawPassword);
        var user = User.builder().email(email).passwordHash(hash).phoneNumber(phoneNumber).build();
        usersRepository.persist(user);

        return new SignUp.Success(jwtService.generateToken(
                email
        ));
    }
}
