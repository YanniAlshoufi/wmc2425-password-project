package at.htlleonding.services.responses;

public class SignUp {
    public sealed interface Response permits Success, UserAlreadyExists, InvalidPassword {}
    public record Success(String jwt) implements Response {}
    public record UserAlreadyExists() implements Response {}
    public record InvalidPassword(String message) implements Response {}
}
