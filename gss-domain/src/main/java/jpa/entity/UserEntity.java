package jpa.entity;

import domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public User toDomainEntity() {
        return new User();
    }

    public static UserEntity from(User domainEntity) {
        return new UserEntity();
    }
}
