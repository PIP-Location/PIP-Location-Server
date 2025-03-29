package kr.co.pinpick.archive.service;

import kr.co.pinpick.archive.dto.request.TagRetrieveRequest;
import kr.co.pinpick.archive.dto.response.TagCollectResponse;
import kr.co.pinpick.archive.dto.response.TagResponse;
import kr.co.pinpick.archive.repository.tag.TagRepository;
import kr.co.pinpick.common.dto.PaginateResponse;
import kr.co.pinpick.common.extension.ListExtension;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@ExtensionMethod(ListExtension.class)
public class TagService {
    private final TagRepository repository;

    @Scheduled(initialDelay = 1000, fixedDelay = 1000 * 60 * 5) // 5분마다
    @Transactional
    public void collectTagCount() {
        log.info("start collect tag count");

        repository.collectTagCount();

        log.info("done collect tag count");
    }

    @Transactional(readOnly = true)
    public TagCollectResponse search(TagRetrieveRequest request) {
        var tags = repository.findAllByName(request);
        // 정확히 일치하는 태그는 최상단으로 이동
        var eq = tags.removeFirst(o -> o.getName().equals(request.getQ()));
        if (eq != null) tags.add(0, eq);

        return TagCollectResponse.builder()
                .collect(tags.stream().map(TagResponse::fromEntity).toList())
                .meta(PaginateResponse.builder().count(tags.size()).build())
                .build();
    }
}
