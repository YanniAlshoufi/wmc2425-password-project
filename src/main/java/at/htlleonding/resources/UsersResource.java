package at.htlleonding.resources;

import at.htlleonding.dtos.ResetPwDto;
import at.htlleonding.dtos.SetNewPwDto;
import at.htlleonding.dtos.SignInReq;
import at.htlleonding.dtos.SignUpReq;
import at.htlleonding.services.UserPasswordResettingService;
import at.htlleonding.services.UserSigningService;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
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
    public Response signIn(SignInReq dto) {
        var optionalJwt = this.signingService.login(dto.username(), dto.password());

        if (optionalJwt.isPresent()) {
            return Response.ok(optionalJwt.get()).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("/sign-up")
    public Response signUp(SignUpReq dto) {
        var optionalJwt = this.signingService.signUp(dto.username(), dto.telephoneNumber(), dto.password());

        if (optionalJwt.isPresent()) {
            return Response.ok(optionalJwt.get()).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @Path("/forgot")
    public Response resetPassword(ResetPwDto resetPwDto) {
        throw new RuntimeException("Not Implemented");
    }
    
    @POST
    @Path("/set-new-password")
    public Response setNewPassword(SetNewPwDto setNewPwDto) {
        throw new RuntimeException("Not Implemented");
    }
}
