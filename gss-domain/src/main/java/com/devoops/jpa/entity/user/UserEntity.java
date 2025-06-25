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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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

    public static UserEntity from(User domainEntity) {
        return new UserEntity(
                null,
                domainEntity.getNickname(),
                domainEntity.getProfileImageUrl(),
                domainEntity.getProviderId()
        );
    }

    public User toDomainEntity() {
        return new User(
                this.providerId,
                this.nickname,
                this.profileImageUrl
        );
    }
}
