package com.devoops.jpa.entity.user;

import com.devoops.domain.entity.user.User;
import jakarta.persistence.*;
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

    private long providerId;

    private String nickname;

    private String profileImageUrl;


    public User toDomainEntity() {
        return new User(
                this.providerId,
                this.nickname,
                this.profileImageUrl
        );
    }

    public static UserEntity from(User domainEntity) {
        return new UserEntity(
                null,
                domainEntity.getProviderId(),
                domainEntity.getNickname(),
                domainEntity.getProfileImageUrl()
        );
    }
}
