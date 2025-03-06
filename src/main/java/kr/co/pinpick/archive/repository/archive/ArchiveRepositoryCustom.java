package kr.co.pinpick.archive.repository.archive;

import kr.co.pinpick.archive.dto.ArchiveRetrieveRequest;
import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.user.entity.User;

import java.util.List;

public interface ArchiveRepositoryCustom {
    List<Archive> retrieve(User user, ArchiveRetrieveRequest request);
}
