package dto.request;

public record GithubTokenRequest(
        String code,
        String redirect_uri
) {

}
