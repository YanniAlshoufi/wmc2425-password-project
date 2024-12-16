package at.htlleonding.users;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/api/v1/users")
public class UsersResource {
    private final UserPasswordResettingService resettingService;
    private final UserSigningService signingService;

    public UsersResource(UserPasswordResettingService resettingService, UserSigningService signingService) {
        this.resettingService = resettingService;
        this.signingService = signingService;
    }

    @POST
    @Path("/sign-in")
    public Response signIn() {
        throw new RuntimeException("Not Implemented");
    }

    @POST
    @Path("/sign-up")
    public Response signUp() {
        throw new RuntimeException("Not Implemented");
    }

    @POST
    @Path("/forgot")
    public Response resetPassword() {
        throw new RuntimeException("Not Implemented");
    }
}
