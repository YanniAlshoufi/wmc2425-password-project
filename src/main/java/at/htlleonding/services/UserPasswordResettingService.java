package at.htlleonding.services;

import at.htlleonding.models.User;
import at.htlleonding.repositories.UsersRepository;
import at.htlleonding.services.responses.ResetPassword;
import at.htlleonding.services.responses.SendResetCode;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class UserPasswordResettingService {
    private final UsersRepository usersRepository;
    private final HashingService hashingService;

    private final ConcurrentHashMap<User, String> oneTimeResetCodes = new ConcurrentHashMap<>();

    private final SecureRandom random = new SecureRandom();
    private final char[] ALLOWED_ONE_TIME_CODE_CHARS = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9', '-', 'a', 'b', 'c', 'd', 'e', 'f', 'g'};

    public UserPasswordResettingService(UsersRepository usersRepository, HashingService hashingService) {
        this.usersRepository = usersRepository;
        this.hashingService = hashingService;
    }

    public SendResetCode.Response sendPasswordResetCode(String email) {
        Optional<User> optionalUser = usersRepository
                .find("email", email)
                .firstResultOptional();

        if (optionalUser.isEmpty()) {
            return new SendResetCode.UserNotFound();
        }

        User user = optionalUser.get();
        String resetCode = generateOneTimeCode();

        log.info("Creating and sending one time reset code \"{}\" for user \"{}\"", resetCode, user.getEmail());

        this.oneTimeResetCodes.put(user, resetCode);

        { //? This should be changed in production to an actual email being sent.
            log.info("You have requested a change of password, here is your reset code: {}", resetCode);
        }

        return new SendResetCode.Sent();
    }

    public ResetPassword.Response trySetNewPw(String email, String oneTimeResetCode, String newPassword) {
        Optional<User> optionalUser = usersRepository
                .find("email", email)
                .firstResultOptional();

        if (optionalUser.isEmpty()) {
            return new ResetPassword.UserNotFound();
        }

        if (!Helpers.isPasswordValid(newPassword)) {
            return new ResetPassword.InvalidPassword(Helpers.PASSWORD_INVALID_MESSAGE);
        }

        User user = optionalUser.get();

        if (!this.oneTimeResetCodes.containsKey(user)) {
            return new ResetPassword.NoResetPasswordSet();
        }

        if (!Objects.equals(this.oneTimeResetCodes.get(user), oneTimeResetCode)) {
            return new ResetPassword.IncorrectResetCodeForPassword();
        }

        var hash = this.hashingService.hash(newPassword);
        this.usersRepository.update("passwordHash = ?1 where email = ?2", hash, email);

        return new ResetPassword.Success();
    }

    private String generateOneTimeCode() {
        return this.random
                .ints(10)
                .map(n -> Math.abs(n) % this.ALLOWED_ONE_TIME_CODE_CHARS.length)
                .mapToObj(n -> this.ALLOWED_ONE_TIME_CODE_CHARS[n])
                .map(c -> Character.toString(c))
                .collect(Collectors.joining());
    }
}
