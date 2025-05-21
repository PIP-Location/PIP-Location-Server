package kr.co.pinpick.user.repository.user;

import kr.co.pinpick.search.dto.request.SearchRequest;
import kr.co.pinpick.user.entity.User;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> search(User principal, SearchRequest request);
}
