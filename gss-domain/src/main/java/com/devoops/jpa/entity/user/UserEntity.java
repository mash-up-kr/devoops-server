package com.devoops.jpa.entity.user;

import com.devoops.domain.entity.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nickname;

    @NotNull
    private String profileImageUrl;

    private long providerId;

    public User toDomainEntity() {
        return new User(
                this.nickname,
                this.profileImageUrl,
                this.providerId
        );
    }

    public static UserEntity from(User domainEntity) {
        return new UserEntity(
                null,
                domainEntity.getNickname(),
                domainEntity.getProfileImageUrl(),
                domainEntity.getProviderId()
        );
    }
}
