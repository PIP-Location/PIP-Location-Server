package kr.co.pinpick.archive.repository.ArchiveComment;

import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.entity.ArchiveComment;
import kr.co.pinpick.common.dto.request.PaginateRequest;

import java.util.List;

public interface ArchiveCommentRepositoryCustom {
    List<ArchiveComment> findByArchiveAndParentIsNull(Archive archive, PaginateRequest request);
}
