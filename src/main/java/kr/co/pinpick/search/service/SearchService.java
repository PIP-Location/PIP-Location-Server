package kr.co.pinpick.search.service;

import kr.co.pinpick.archive.repository.archive.ArchiveRepository;
import kr.co.pinpick.archive.repository.tag.TagRepository;
import kr.co.pinpick.common.dto.response.PaginateResponse;
import kr.co.pinpick.common.extension.ListExtension;
import kr.co.pinpick.search.dto.request.SearchRequest;
import kr.co.pinpick.search.dto.response.ArchiveSearchResponse;
import kr.co.pinpick.search.dto.response.SearchCollectResponse;
import kr.co.pinpick.search.dto.response.TagSearchResponse;
import kr.co.pinpick.search.dto.response.UserSearchResponse;
import kr.co.pinpick.user.entity.User;
import kr.co.pinpick.user.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@ExtensionMethod(ListExtension.class)
public class SearchService {
    private final ArchiveRepository archiveRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public Object search(SearchRequest request) {
        return null;
    }

    @Transactional(readOnly = true)
    public SearchCollectResponse<ArchiveSearchResponse> searchArchive(SearchRequest request) {
        var searchKeywords =  archiveRepository.searchKeyword(request);
        // 정확히 일치하는 키워드는 최상단으로 이동
        var eq = searchKeywords.removeFirst(o -> o.equals(request.getQ()));
        if (eq != null) searchKeywords.add(0, eq);

        List<ArchiveSearchResponse> results = new ArrayList<>();
        for (String keyword : searchKeywords) {
            long count = archiveRepository.countContainKeyword(request);
            results.add(ArchiveSearchResponse.from(keyword, count));
        }

        return SearchCollectResponse.<ArchiveSearchResponse>builder()
                .collect(results)
                .meta(PaginateResponse.builder().count(searchKeywords.size()).build())
                .build();
    }

    @Transactional(readOnly = true)
    public SearchCollectResponse<UserSearchResponse> searchUser(User user, SearchRequest request) {
        var users = userRepository.search(user, request);
        // 정확히 일치하는 키워드는 최상단으로 이동
        var eq = users.removeFirst(o -> o.getNickname().equals(request.getQ()));
        if (eq != null) users.add(0, eq);

        return SearchCollectResponse.<UserSearchResponse>builder()
                .collect(users.stream().map(UserSearchResponse::fromEntity).toList())
                .meta(PaginateResponse.builder().count(users.size()).build())
                .build();
    }

    @Transactional(readOnly = true)
    public SearchCollectResponse<TagSearchResponse> searchTag(SearchRequest request) {
        var tags = tagRepository.search(request);
        // 정확히 일치하는 키워드는 최상단으로 이동
        var eq = tags.removeFirst(o -> o.getName().equals(request.getQ()));
        if (eq != null) tags.add(0, eq);

        return SearchCollectResponse.<TagSearchResponse>builder()
                .collect(tags.stream().map(TagSearchResponse::fromEntity).toList())
                .meta(PaginateResponse.builder().count(tags.size()).build())
                .build();
    }
}
