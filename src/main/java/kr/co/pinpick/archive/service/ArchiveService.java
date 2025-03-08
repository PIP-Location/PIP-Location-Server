package kr.co.pinpick.archive.service;

import kr.co.pinpick.archive.dto.ArchiveCollectResponse;
import kr.co.pinpick.archive.dto.ArchiveResponse;
import kr.co.pinpick.archive.dto.ArchiveRetrieveRequest;
import kr.co.pinpick.archive.dto.CreateArchiveRequest;
import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArchiveService {
    ArchiveResponse create(User author, CreateArchiveRequest request, List<MultipartFile> attaches);

    ArchiveCollectResponse retrieve(User user, ArchiveRetrieveRequest request);

    void delete(Archive archive);

    Boolean changeIsPublic(Archive archive, boolean isPublic);

    void like(User user, Archive archive);

    void dislike(User user, Archive archive);
}
