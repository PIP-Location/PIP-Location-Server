package kr.co.pinpick.folder;

import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;
import kr.co.pinpick.user.dto.request.CreateFolderRequest;
import kr.co.pinpick.user.dto.response.FolderCollectResponse;
import kr.co.pinpick.user.dto.response.FolderResponse;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FolderSteps {
    public static FolderResponse createFolder(String token, CreateFolderRequest request, InputStream file) {
        var client = RestAssured
                .given()
                .multiPart("attach", "image.jpg", file, "image/jpeg");

        return client
                .log().all()
                .contentType(ContentType.MULTIPART.withCharset("UTF-8"))
                .multiPart(new MultiPartSpecBuilder(request).controlName("request").mimeType("application/json").charset(StandardCharsets.UTF_8).build())
                .when()
                .auth().oauth2(token)
                .post("/api/folders")
                .then().log().all()
                .statusCode(201)
                .extract().as(FolderResponse.class);
    }

    public static FolderCollectResponse getFolderList(String token) {
        return RestAssured
                .given().log().all()
                .when()
                .auth().oauth2(token)
                .get("/api/folders")
                .then().log().all()
                .statusCode(200)
                .extract().as(FolderCollectResponse.class);
    }

    public static void addArchiveToFolder(String token, long folderId, long archiveId) {
         RestAssured
            .given().log().all()
            .contentType(ContentType.JSON.withCharset("UTF-8"))
            .when()
            .auth().oauth2(token)
            .post("/api/folders/{folderId}/archives/{archiveId}", folderId, archiveId)
            .then().log().all()
            .statusCode(204)
            .extract();
    }

    public static void removeArchiveToFolder(String token, long folderId, long archiveId) {
        RestAssured
            .given().log().all()
            .contentType(ContentType.JSON.withCharset("UTF-8"))
            .when()
            .auth().oauth2(token)
            .delete("/api/folders/{folderId}/archives/{archiveId}", folderId, archiveId)
            .then().log().all()
            .statusCode(204)
            .extract();
    }
}
