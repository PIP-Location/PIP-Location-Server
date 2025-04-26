package kr.co.pinpick.archive.repository.archive;

import kr.co.pinpick.archive.dto.request.ArchiveRetrieveRequest;
import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.common.dto.request.OffsetPaginateRequest;
import kr.co.pinpick.common.dto.request.SearchRequest;
import kr.co.pinpick.user.entity.User;

import java.util.List;

public interface ArchiveRepositoryCustom {
    List<Archive> retrieve(User principal, ArchiveRetrieveRequest request);

    List<Archive> findAllByUser(User principal, User user);

    List<Archive> search(SearchRequest request);

    List<User> findRepipByArchive(Archive source, OffsetPaginateRequest request);
}
