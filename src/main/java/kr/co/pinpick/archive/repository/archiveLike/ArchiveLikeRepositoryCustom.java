package kr.co.pinpick.archive.repository.archiveLike;

import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.common.dto.request.OffsetPaginateRequest;
import kr.co.pinpick.user.entity.User;

import java.util.List;

public interface ArchiveLikeRepositoryCustom {
    List<User> findLikeByArchive(Archive source, OffsetPaginateRequest request);
}
