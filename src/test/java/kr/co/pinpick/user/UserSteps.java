package kr.co.pinpick.user;

import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import kr.co.pinpick.common.dto.response.BaseResponse;
import kr.co.pinpick.user.dto.request.LoginRequest;
import kr.co.pinpick.user.dto.request.SignupRequest;
import kr.co.pinpick.user.dto.request.UpdateUserRequest;
import kr.co.pinpick.user.dto.response.TokenResponse;
import kr.co.pinpick.user.dto.response.UserDetailResponse;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class UserSteps {

    public static Long signUp(SignupRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .body(request)
                .when()
                .post("/api/auth/signUp")
                .then().log().all()
                .statusCode(201)
                .extract().as(Long.class);
    }

    public static TokenResponse testLogin(LoginRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .body(request)
                .when()
                .post("/api/auth/testLogin")
                .then().log().all()
                .statusCode(200)
                .extract().as(TokenResponse.class);
    }

    public static void block(String token, Long userId) {
        RestAssured
            .given().log().all()
            .contentType(ContentType.JSON.withCharset("UTF-8"))
            .when()
            .auth().oauth2(token)
            .post("/api/users/{userId}/block", userId)
            .then().log().all()
            .statusCode(200);
    }

    public static UserDetailResponse update(String token, UpdateUserRequest request, InputStream file) {
        return RestAssured
                .given().log().all()
                .multiPart("attach", "image.jpg", file, "image/jpeg")
                .contentType(ContentType.MULTIPART.withCharset("UTF-8"))
                .multiPart(new MultiPartSpecBuilder(request).controlName("request").mimeType("application/json").charset(StandardCharsets.UTF_8).build())
                .when()
                .auth().oauth2(token)
                .patch("/api/users")
                .then().log().all()
                .extract().as(new TypeRef<BaseResponse<UserDetailResponse>>() {}).getData();
    }
}
