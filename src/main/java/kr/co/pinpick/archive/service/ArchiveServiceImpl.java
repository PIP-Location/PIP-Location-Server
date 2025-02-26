package kr.co.pinpick.archive.service;

import kr.co.pinpick.archive.dto.ArchiveResponse;
import kr.co.pinpick.archive.dto.CreateArchiveRequest;
import kr.co.pinpick.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArchiveServiceImpl implements ArchiveService {

    @Override
    @Transactional
    public ArchiveResponse create(User author, CreateArchiveRequest request, List<MultipartFile> attaches) {
        return null;
    }
}
