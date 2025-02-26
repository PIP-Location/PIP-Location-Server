package kr.co.pinpick.archive.service;

import kr.co.pinpick.archive.dto.ArchiveResponse;
import kr.co.pinpick.archive.dto.CreateArchiveRequest;
import kr.co.pinpick.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArchiveService {
    ArchiveResponse create(User author, CreateArchiveRequest request, List<MultipartFile> attaches);
}
