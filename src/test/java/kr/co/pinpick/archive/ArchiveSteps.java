package kr.co.pinpick.archive;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import kr.co.pinpick.archive.dto.request.CreateArchiveRequest;
import kr.co.pinpick.archive.dto.response.ArchiveCollectResponse;
import kr.co.pinpick.archive.dto.response.ArchiveDetailResponse;
import kr.co.pinpick.archive.dto.request.ArchiveRetrieveRequest;
import kr.co.pinpick.common.dto.response.BaseResponse;
import kr.co.pinpick.user.dto.response.FolderCollectResponse;
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

    public static ArchiveDetailResponse successCreateArchive(String token, CreateArchiveRequest request, InputStream... files) {
        return createArchive(token, request, files)
                .statusCode(201)
                .extract().as(new TypeRef<BaseResponse<ArchiveDetailResponse>>() {}).getData();
    }

    public static BaseResponse<Void> failCreateArchiveBecauseValidation(String token, CreateArchiveRequest request, InputStream... files) {
        return createArchive(token, request, files)
                .statusCode(400)
                .extract().as(BaseResponse.class);
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
                .extract().as(new TypeRef<BaseResponse<ArchiveCollectResponse>>() {}).getData();
    }

    public static ArchiveCollectResponse getByFolder(String token, long folderId) {
        return RestAssured
                .given().log().all()
                .when()
                .auth().oauth2(token)
                .get("/api/archives/folders/{folderId}", folderId)
                .then().log().all()
                .statusCode(200)
                .extract().as(new TypeRef<BaseResponse<ArchiveCollectResponse>>() {}).getData();
    }

    public static ArchiveDetailResponse successRetrieveById(String token, long archiveId) {
        return RestAssured
                .given().log().all()
                .when()
                .auth().oauth2(token)
                .get("/api/archives/{archiveId}", archiveId)
                .then().log().all()
                .statusCode(200)
                .extract().as(ArchiveDetailResponse.class);
    }

    public static BaseResponse<Void> failRetrieveById(String token, long archiveId) {
        return RestAssured
                .given().log().all()
                .when()
                .auth().oauth2(token)
                .get("/api/archives/{archiveId}", archiveId)
                .then().log().all()
                .statusCode(404)
                .extract().as(new TypeRef<BaseResponse<Void>>() {});
    }

    public static void successDeleteArchive(String token, long archiveId) {
        RestAssured
            .given().log().all()
            .when()
            .auth().oauth2(token)
            .delete("/api/archives/{archiveId}", archiveId)
            .then().log().all()
            .statusCode(200)
            .extract();
    }

    public static BaseResponse<Void> failDeleteArchive(String token, long archiveId) {
        return RestAssured
                .given().log().all()
                .when()
                .auth().oauth2(token)
                .delete("/api/archives/{archiveId}", archiveId)
                .then().log().all()
                .statusCode(403)
                .extract().as(new TypeRef<BaseResponse<Void>>() {});
    }
}
