package at.htlleonding.resources;

import at.htlleonding.dtos.*;
import at.htlleonding.services.UserPasswordResettingService;
import at.htlleonding.services.SignService;
import at.htlleonding.services.responses.ResetPassword;
import at.htlleonding.services.responses.SendResetCode;
import at.htlleonding.services.responses.SignIn;
import at.htlleonding.services.responses.SignUp;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/api/v1/users")
public class UsersResource {
    private final UserPasswordResettingService resettingService;
    private final SignService signingService;

    public UsersResource(
            UserPasswordResettingService resettingService,
            SignService signingService
    ) {
        this.resettingService = resettingService;
        this.signingService = signingService;
    }


    @POST
    @Path("/sign-in")
    public Response signIn(@Valid SignInReq dto) {
        var response = this.signingService.signIn(dto.username(), dto.password());

        return switch (response) {
            case SignIn.Success(var jwt) -> Response.ok(new JwtRes(jwt)).build();
            case SignIn.UserNotFound() -> Response.status(Response.Status.NOT_FOUND).build();
            case SignIn.IncorrectPassword() -> Response.status(Response.Status.UNAUTHORIZED).build();
        };
    }

    @POST
    @Path("/sign-up")
    public Response signUp(@Valid SignUpReq dto) {
        var response = this.signingService.signUp(dto.username(), dto.telephoneNumber(), dto.password());

        return switch (response) {
            case SignUp.Success(var jwt) -> Response.ok(new JwtRes(jwt)).build();
            case SignUp.UserAlreadyExists() -> Response.status(Response.Status.CONFLICT).build();
            case SignUp.InvalidPassword(var message) ->
                    Response.status(Response.Status.BAD_REQUEST).entity(new ErrorRes(message)).build();
        };
    }

    @POST
    @Path("/forgot-password")
    public Response resetPassword(@Valid ResetPasswordReq resetPasswordReq) {
        var response = this.resettingService.sendPasswordResetCode(resetPasswordReq.email());

        return switch (response) {
            case SendResetCode.Sent() -> Response.noContent().build();
            case SendResetCode.UserNotFound() -> Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        };
    }

    @POST
    @Path("/set-new-password")
    public Response setNewPassword(@Valid SetNewPasswordReq setNewPasswordReq) {
        var response = this.resettingService.trySetNewPw(
                setNewPasswordReq.email(),
                setNewPasswordReq.oneTimeCode(),
                setNewPasswordReq.newPassword()
        );

        return switch (response) {
            case ResetPassword.Success() -> Response.noContent().build();

            case ResetPassword.NoResetPasswordSet() -> Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorRes("There is no request for a password reset!"))
                    .build();

            case ResetPassword.IncorrectResetCodeForPassword() -> Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorRes("Incorrect reset code!"))
                    .build();

            case ResetPassword.UserNotFound() -> Response.status(Response.Status.NOT_FOUND).build();

            case ResetPassword.InvalidPassword(var message) -> Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorRes(message))
                    .build();
        };
    }
}
