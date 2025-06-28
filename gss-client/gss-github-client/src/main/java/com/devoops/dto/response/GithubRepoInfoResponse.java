package com.devoops.dto.response;

public record GithubRepoInfoResponse(
        long id,
        String name,
        String url,
        OwnerResponse owner
) {

    public String getOwnerName() {
        return owner.name();
    }
}
