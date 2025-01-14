package at.htlleonding.services;

import java.util.regex.Pattern;

public class Helpers {
    private final static Pattern PASSWORD_REGEX = Pattern.compile(
            "^(?=(?:.*[A-Z])+)(?=(?:.*[a-z])+)(?=(?:.*\\d)+)(?=(?:.*[!@#$%^&*()\\-_=+{};:,<.>])+)([A-Za-z0-9!@#$%^&*()\\-_=+{};:,<.>]{6,})$"
    );

    public static boolean isPasswordValid(String password) {
        return PASSWORD_REGEX.matcher(password).matches();
    }

    public static final String PASSWORD_INVALID_MESSAGE = "Your password is either too weak or has invalid characters. A password be at least 6 characters, must have lowercase letters, uppercase letters, at least one number, and at least one of the allowed special characters: !@#$%^&*()\\-_=+{};:,<.>).";
}
