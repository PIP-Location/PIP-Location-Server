package kr.co.pinpick.archive.repository.tag;

import kr.co.pinpick.archive.entity.Tag;
import kr.co.pinpick.search.dto.request.SearchRequest;

import java.util.List;

public interface TagRepositoryCustom {
    List<Tag> search(SearchRequest request);
}
