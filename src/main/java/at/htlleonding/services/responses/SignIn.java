package at.htlleonding.services.responses;

public class SignIn {
    public sealed interface Response permits Success, UserNotFound, IncorrectPassword {}
    public record Success(String jwt) implements Response {}
    public record UserNotFound() implements Response {}
    public record IncorrectPassword() implements Response {}
}
