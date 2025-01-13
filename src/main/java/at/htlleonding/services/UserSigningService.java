package at.htlleonding.services;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class UserSigningService {
    private final HashingService hashingService;

    public UserSigningService(HashingService hashingService) {
        this.hashingService = hashingService;
    }

    public Optional<String> login() {

    }
}
