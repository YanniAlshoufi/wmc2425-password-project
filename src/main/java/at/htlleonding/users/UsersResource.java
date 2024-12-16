package at.htlleonding.users;

import at.htlleonding.users.dtos.SetNewPwDto;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Slf4j
@Path("/api/v1/users")
public class UsersResource {
    private final UserPasswordResettingService resettingService;
    private final UserSigningService signingService;
    private final String pepper;

    public UsersResource(
            UserPasswordResettingService resettingService,
            UserSigningService signingService,
            @ConfigProperty(name = "QUARKUS_PASSWORDS_HASH_PEPPER") String pepper
    ) {
        this.resettingService = resettingService;
        this.signingService = signingService;
        this.pepper = pepper;
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
    
    @POST
    @Path("/set-new-pw")
    public Response setNewPassword(SetNewPwDto setNewPwDto) {
        throw new RuntimeException("Not implemented");
    }
}
