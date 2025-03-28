package kr.co.pinpick.user.repository.user;

import kr.co.pinpick.user.dto.request.UserRetrieveRequest;
import kr.co.pinpick.user.entity.User;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> retrieve(User author, UserRetrieveRequest request);
}
