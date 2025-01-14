package at.htlleonding.services.responses;

public class ResetPassword {
    public sealed interface Response permits Success, UserNotFound, NoResetPasswordSet, IncorrectResetCodeForPassword, InvalidPassword {
    }

    public record Success() implements Response {
    }

    public record UserNotFound() implements Response {
    }

    public record NoResetPasswordSet() implements Response {
    }

    public record IncorrectResetCodeForPassword() implements Response {
    }

    public record InvalidPassword(String message) implements Response {
    }
}
