package at.htlleonding.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SetNewPasswordReq(
        @NotNull
        @NotEmpty
        @Email
        String email,

        @NotNull
        @NotEmpty
        @Size(min = 4, max = 25) // This is just to stay flexible when the length is updated
        String oneTimeCode,

        @NotNull
        @NotEmpty
        @Size(min = 6)
        String newPassword
) {}
