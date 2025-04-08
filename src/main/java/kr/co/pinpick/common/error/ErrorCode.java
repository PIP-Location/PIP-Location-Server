package kr.co.pinpick.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_TYPE_VALUE(400, "C001", "Invalid type value"),
    INVALID_INPUT_VALUE(400, "C003", "Invalid input value"),
    METHOD_NOT_ALLOWED(405, "C004", "Method not allowed"),
    ACCESS_DENIED(403, "C005", "Access denied"),
    INTERNAL_SERVER_ERROR(500, "C006", "Internal server error"),
    ENTITY_NOT_FOUND(404, "C007", "Not Found"),


    // User
    EMAIL_DUPLICATION(400, "U001", "Email is duplication"),


    // FolderArchive
    ARCHIVE_ALREADY_ADDED(400, "FA001", "Already Added archive"),
    ARCHIVE_ALREADY_REMOVED(400, "FA002", "Already removed archive"),
    ONLY_ADDABLE_FOLLOWERS_ARCHIVE(400, "FA003", "Only addable follower's archive"),


    // Link
    USER_ALREADY_LINKED(400, "L001", "Already linked"),
    USER_ALREADY_UNLINKED(400, "L002", "Already unlinked"),


    // Auth
    UNAUTHENTICATED(401, "T001", "Unauthenticated access"),
    EXPIRED_JWT_EXCEPTION(402, "T002", "Expired JWT Token"),
    MALFORMED_JWT_EXCEPTION(403, "T003", "Invalid JWT Token"),
    UNSUPPORTED_JWT_EXCEPTION(404, "T004", "Unsupported JWT Token"),
    ILLEGAL_ARGUMENT_EXCEPTION(405, "T005", "JWT claims string is empty."),
    SIGNATURE_JWT_EXCEPTION(406, "T006", "Modulated JWT Token");


    private final int status;
    private final String code;
    private final String message;
}
