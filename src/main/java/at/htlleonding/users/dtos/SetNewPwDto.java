package at.htlleonding.users.dtos;

public record SetNewPwDto(String email, String oneTimeCode, String newPw) {}
