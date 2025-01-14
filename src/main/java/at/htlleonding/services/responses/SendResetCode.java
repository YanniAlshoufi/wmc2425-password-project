package at.htlleonding.services.responses;

public class SendResetCode {
    public sealed interface Response permits Sent, UserNotFound {
    }

    public record Sent() implements Response {
    }

    public record UserNotFound() implements Response {
    }
}
