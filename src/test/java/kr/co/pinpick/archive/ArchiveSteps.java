package kr.co.pinpick.archive;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import kr.co.pinpick.archive.dto.request.CreateArchiveRequest;
import kr.co.pinpick.archive.dto.response.ArchiveCollectResponse;
import kr.co.pinpick.archive.dto.response.ArchiveResponse;
import kr.co.pinpick.archive.dto.response.ArchiveRetrieveRequest;
import kr.co.pinpick.common.error.ErrorResponse;
import kr.co.pinpick.user.dto.response.FolderDetailResponse;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ArchiveSteps {

    private static ValidatableResponse createArchive(String token, CreateArchiveRequest request, InputStream... files) {
        var client = RestAssured.given();

        for (var file : files) {
            client.multiPart("attaches", "image.jpg", file, "image/jpeg");
        }

        return client
                .log().all()
                .contentType(ContentType.MULTIPART.withCharset("UTF-8"))
                .multiPart(new MultiPartSpecBuilder(request).controlName("request").mimeType("application/json").charset(StandardCharsets.UTF_8).build())
                .when()
                .auth().oauth2(token)
                .post("/api/archives")
                .then().log().all();
    }

    public static ArchiveResponse successCreateArchive(String token, CreateArchiveRequest request, InputStream... files) {
        return createArchive(token, request, files)
                .statusCode(201)
                .extract().as(ArchiveResponse.class);
    }

    public static ErrorResponse failCreateArchiveBecauseValidation(String token, CreateArchiveRequest request, InputStream... files) {
        return createArchive(token, request, files)
                .statusCode(400)
                .extract().as(ErrorResponse.class);
    }

    public static ArchiveCollectResponse retrieve(String token, ArchiveRetrieveRequest request) {
        return RestAssured
                .given().log().all()
                .when()
                .auth().oauth2(token)
                .queryParams(new ObjectMapper().convertValue(request, Map.class))
                .get("/api/archives")
                .then().log().all()
                .statusCode(200)
                .extract().as(ArchiveCollectResponse.class);
    }

    public static ArchiveCollectResponse getByFolder(String token, long folderId) {
        return RestAssured
                .given().log().all()
                .when()
                .auth().oauth2(token)
                .get("/api/archives/folders/{folderId}", folderId)
                .then().log().all()
                .statusCode(200)
                .extract().as(ArchiveCollectResponse.class);
    }

    public static FolderDetailResponse getArchivesWithFolderInfo(String token, long folderId) {
        return RestAssured
                .given().log().all()
                .when()
                .auth().oauth2(token)
                .get("/api/folders/{folderId}", folderId)
                .then().log().all()
                .statusCode(200)
                .extract().as(FolderDetailResponse.class);
    }

    public static boolean successChangeIsPublic(String token, long archiveId) {
        return RestAssured
            .given().log().all()
            .when()
            .auth().oauth2(token)
            .patch("/api/archives/{archiveId}/public", archiveId)
            .then().log().all()
            .statusCode(200)
            .extract().as(Boolean.class);
    }

    public static ErrorResponse failChangeIsPublic(String token, long archiveId, boolean isPublic) {
        return RestAssured
                .given().log().all()
                .when()
                .auth().oauth2(token)
                .patch("/api/archives/{archiveId}/public/{isPublic}", archiveId, isPublic)
                .then().log().all()
                .statusCode(401)
                .extract().as(ErrorResponse.class);
    }

    public static void successChangeIsPublic(String token, long archiveId, boolean isPublic) {
        RestAssured
                .given().log().all()
                .when()
                .auth().oauth2(token)
                .patch("/api/archives/{archiveId}/public/{isPublic}", archiveId, isPublic)
                .then().log().all()
                .statusCode(200)
                .extract().as(Boolean.class);
    }

    public static ArchiveResponse successRetrieveById(String token, long archiveId) {
        return RestAssured
                .given().log().all()
                .when()
                .auth().oauth2(token)
                .get("/api/archives/{archiveId}", archiveId)
                .then().log().all()
                .statusCode(200)
                .extract().as(ArchiveResponse.class);
    }

    public static ErrorResponse failRetrieveById(String token, long archiveId) {
        return RestAssured
                .given().log().all()
                .when()
                .auth().oauth2(token)
                .get("/api/archives/{archiveId}", archiveId)
                .then().log().all()
                .statusCode(404)
                .extract().as(ErrorResponse.class);
    }

    public static void successDeleteArchive(String token, long archiveId) {
        RestAssured
            .given().log().all()
            .when()
            .auth().oauth2(token)
            .delete("/api/archives/{archiveId}", archiveId)
            .then().log().all()
            .statusCode(204)
            .extract();
    }

    public static ErrorResponse failDeleteArchive(String token, long archiveId) {
        return RestAssured
                .given().log().all()
                .when()
                .auth().oauth2(token)
                .delete("/api/archives/{archiveId}", archiveId)
                .then().log().all()
                .statusCode(401)
                .extract().as(ErrorResponse.class);
    }
}
