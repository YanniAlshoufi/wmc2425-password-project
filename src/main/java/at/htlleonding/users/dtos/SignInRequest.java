package at.htlleonding.users.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SignInRequest(
        @Email(message = "Please provide a valid email.")
        @NotNull(message = "Please provide an email.")
        String email,
        @NotNull(message = "Please provide a password.")
        String password
) {}
