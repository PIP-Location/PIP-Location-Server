package kr.co.pinpick.archive.entity.listener;

import jakarta.persistence.PreRemove;
import kr.co.pinpick.archive.entity.ArchiveAttach;
import kr.co.pinpick.common.storage.IStorageManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ArchiveAttachListener {
    private IStorageManager storageManager;

    @Autowired
    public void init(ApplicationContext applicationContext) {
        storageManager = applicationContext.getBean(IStorageManager.class);
    }

    @PreRemove
    public void delete(ArchiveAttach archiveAttach) {
        log.info("start delete file from storage");

        storageManager.delete(archiveAttach.getPath());

        log.info("done delete file from storage");
    }
}
