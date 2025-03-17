package kr.co.pinpick.user.service;

import kr.co.pinpick.common.error.EntityNotFoundException;
import kr.co.pinpick.common.error.ErrorCode;
import kr.co.pinpick.user.dto.request.UpdateUserRequest;
import kr.co.pinpick.user.dto.response.UserDetailResponse;
import kr.co.pinpick.user.entity.User;
import kr.co.pinpick.user.repository.FollowerRepository;
import kr.co.pinpick.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;

    @Transactional
    public UserDetailResponse find(User user, User target) {
        target = userRepository.findById(target.getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND, ""));
        boolean isFollow = followerRepository.existsByFollowerAndFollow(user, target);
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
