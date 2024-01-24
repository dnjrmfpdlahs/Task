package aladdinsys.api.dto;

public record MemberDTO (
    String userId,
    String password,
    String name,
    String regNo
) {

}
