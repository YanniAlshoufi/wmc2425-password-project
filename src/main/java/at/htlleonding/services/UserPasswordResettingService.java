package at.htlleonding.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.htlleonding.models.User;
import at.htlleonding.repositories.UsersRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class UserPasswordResettingService {
   @ConfigProperty(name = "WMC_QUARKUS_PASSWORDS_HASH_PEPPER")
   String pepper;

   @Inject
   UsersRepository usersRepository;

   Map<User, String> oneTimeResetCodes = new HashMap<>();

   final SecureRandom random = new SecureRandom();
   final char[] ALLOWED_ONE_TIME_CODE_CHARS = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9', '-', 'a', 'b', 'c', 'd', 'e', 'f', 'g'};
   
   public boolean sendOneTimeCode(String email) {
      Optional<User> optionalUser = usersRepository
              .find("User user where user.email = :email", email)
              .firstResultOptional();

      if (optionalUser.isEmpty()) {
         return false;
      }

      User user = optionalUser.get();
      String resetCode = this.random
            .ints(20)
            .map(n -> n % this.ALLOWED_ONE_TIME_CODE_CHARS.length)
            .mapToObj(n -> this.ALLOWED_ONE_TIME_CODE_CHARS[n])
            .map(c -> Character.toString(c))
            .collect(Collectors.joining());

      log.info("Creating and sending one time reset code \"{}\" for user \"{}\"", resetCode, user.getEmail());

      this.oneTimeResetCodes.put(user, resetCode);
      return true;
   }
   
   public boolean trySetNewPw(String email, String oneTimeResetCode, String newPw) {
      Optional<User> optionalUser = usersRepository
              .find("User user where user.email = :email", email)
              .firstResultOptional();

      if (optionalUser.isEmpty()) {
         return false;
      }

      User user = optionalUser.get();
      if (!this.oneTimeResetCodes.containsKey(user)) {
         return false;
      }
      if (!Objects.equals(this.oneTimeResetCodes.get(user), oneTimeResetCode)) {
         return false;
      }

      user.setPasswordHash(new String(Base64.getEncoder().encode(BCrypt.with(random).hash(6, (newPw + this.pepper).getBytes(StandardCharsets.UTF_8)))));
      this.usersRepository.persist(user);

      return true;
   }
}
