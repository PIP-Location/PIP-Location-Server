package kr.co.pinpick.archive.repository.tag;

import kr.co.pinpick.archive.dto.request.TagRetrieveRequest;
import kr.co.pinpick.archive.entity.Tag;

import java.util.List;

public interface TagRepositoryCustom {
    List<Tag> findAllByName(TagRetrieveRequest request);
}
