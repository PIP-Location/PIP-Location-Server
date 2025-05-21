package kr.co.pinpick.archive.service;

import kr.co.pinpick.archive.repository.tag.TagRepository;
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
}
