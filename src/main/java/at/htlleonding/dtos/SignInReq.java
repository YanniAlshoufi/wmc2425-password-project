package at.htlleonding.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignInReq(
        @NotNull
        @Email
        @NotEmpty
        String username,

        @NotNull
        @NotEmpty
        @Size(min = 6, max = 50)
        String password
) {
}
