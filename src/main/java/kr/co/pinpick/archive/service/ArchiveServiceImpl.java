package kr.co.pinpick.archive.service;

import kr.co.pinpick.archive.dto.ArchiveResponse;
import kr.co.pinpick.archive.dto.CreateArchiveRequest;
import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.entity.ArchiveTag;
import kr.co.pinpick.archive.repository.ArchiveRepository;
import kr.co.pinpick.archive.repository.ArchiveTagRepository;
import kr.co.pinpick.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ArchiveServiceImpl implements ArchiveService {

    private final ArchiveRepository archiveRepository;

    private final ArchiveTagRepository archiveTagRepository;

    private final GeometryFactory geometryFactory;

    @Override
    @Transactional
    public ArchiveResponse create(User author, CreateArchiveRequest request, List<MultipartFile> attaches) {
        if (attaches == null) {
            attaches = new ArrayList<>();
        }

        // 아카이브 생성
        var archive = Archive.builder()
                .location(geometryFactory.createPoint(new Coordinate(request.getPositionX(), request.getPositionY())))
                .positionX(request.getPositionX())
                .positionY(request.getPositionY())
                .address(request.getAddress())
                .name(request.getName())
                .content(request.getContent())
                .isPublic(request.isPublic())
                .author(author)
                .build();

        archiveRepository.save(archive);

        // TODO: Amazon S3 save attaches
        archive.setArchiveAttaches(new ArrayList<>());

        // 태그 저장
        var index = new AtomicInteger();
        List<ArchiveTag> archiveTags = request.getTags()
                .stream()
                .map(o -> ArchiveTag.from(archive, o.getName(), index.getAndIncrement()))
                .toList();
        archiveTagRepository.saveAll(archiveTags);
        archive.setTags(archiveTags);

        return ArchiveResponse.fromEntity(archive, false, false);
    }
}
