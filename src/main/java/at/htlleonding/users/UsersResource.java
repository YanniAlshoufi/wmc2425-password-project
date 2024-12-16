package at.htlleonding.users;

import at.htlleonding.users.dtos.SignInRequest;
import at.htlleonding.users.dtos.SignUpRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/api/v1/users")
public class UsersResource {
    private final UserPasswordResettingService resettingService;
    private final UserSigningService signingService;

    public UsersResource(
            UserPasswordResettingService resettingService,
            UserSigningService signingService
    ) {
        this.resettingService = resettingService;
        this.signingService = signingService;
    }


    @POST
    @Path("/sign-in")
    public Response signIn(@NonNull @Valid SignInRequest req) {
        try {
            boolean result = this.signingService.signIn(req.email(), req.password());
            return result
                    ? Response.ok("You could log in successfully.").build()
                    : Response
                        .status(Response.Status.UNAUTHORIZED)
                        .entity("User doesn't exist or password incorrect.")
                        .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("/sign-up")
    public Response signUp(@NonNull @Valid SignUpRequest req) {
        throw new RuntimeException("Not Implemented");
    }

    @POST
    @Path("/forgot")
    public Response resetPassword() {
        throw new RuntimeException("Not Implemented");
    }
}
