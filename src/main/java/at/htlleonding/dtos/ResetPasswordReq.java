package at.htlleonding.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ResetPasswordReq(
        @NotNull
        @NotEmpty
        @Email
        String email
) {
}
