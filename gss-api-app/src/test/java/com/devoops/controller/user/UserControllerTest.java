package com.devoops.controller.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.devoops.BaseControllerTest;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.response.UserReadResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class UserControllerTest extends BaseControllerTest {


    @Nested
    class GetUser {

        @Test
        void 유저_정보를_조회할_수_있다() {
            User user = userGenerator.generate("김건우");

            UserReadResponse readResponse = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .pathParam("userId", user.getId())
                    .when().get("/api/users/{userId}")
                    .then().statusCode(HttpStatus.OK.value())
                    .extract().as(UserReadResponse.class);

            assertAll(
                    () -> assertThat(readResponse.id()).isEqualTo(user.getId()),
                    () -> assertThat(readResponse.nickname()).isEqualTo(user.getNickname()),
                    () -> assertThat(readResponse.profileImageUrl()).isEqualTo(user.getProfileImageUrl())
            );
        }
    }
}
