package kr.co.pinpick.user.service;

import kr.co.pinpick.common.dto.PaginateResponse;
import kr.co.pinpick.user.dto.request.UpdateUserRequest;
import kr.co.pinpick.user.dto.request.UserRetrieveRequest;
import kr.co.pinpick.user.dto.response.UserCollectResponse;
import kr.co.pinpick.user.dto.response.UserDetailResponse;
import kr.co.pinpick.user.dto.response.UserResponse;
import kr.co.pinpick.user.dto.response.UserSearchResponse;
import kr.co.pinpick.user.entity.User;
import kr.co.pinpick.user.repository.FollowerRepository;
import kr.co.pinpick.user.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;

    @Transactional(readOnly = true)
    public UserSearchResponse search(User user, UserRetrieveRequest request) {
        var users = userRepository.retrieve(user, request);
        return UserSearchResponse.builder()
                .collect(users.stream().map(UserSearchResponse.SearchResponse::fromEntity).toList())
                .meta(PaginateResponse.builder().count(users.size()).build())
                .build();
    }

    @Transactional(readOnly = true)
    public UserDetailResponse find(User user, Long targetId) {
        var target = userRepository.findByIdOrElseThrow(targetId);
        var isFollow = followerRepository.existsByFollowerAndFollow(user, target);
        return UserDetailResponse.fromEntity(target, isFollow);
    }

    @Transactional
    public UserDetailResponse update(User user, UpdateUserRequest request, MultipartFile profileImage) {
        user.setNickName(request.getNickName());
        user.setDescription(request.getDescription());

        // TODO: 프로필 이미지 업데이트

        return UserDetailResponse.fromEntity(user, false);
    }
}
