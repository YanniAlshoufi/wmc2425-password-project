package at.htlleonding.users.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record SignUpRequest(
        @Email(message = "Please provide a valid email.")
        @NotNull(message = "Please provide an email.")
        String email,
        @Length(min = 6, message = "Please provide at least 6 characters for you password.")
        @NotNull(message = "Please provide a password.")
        String password,
        @NotNull(message = "Please provide a phone number.")
        String phoneNumber
) {}
