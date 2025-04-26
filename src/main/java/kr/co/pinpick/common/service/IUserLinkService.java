package kr.co.pinpick.common.service;

import kr.co.pinpick.user.entity.User;

public interface IUserLinkService {
    void link(User source, Long targetId);
    int unlink(User source, Long targetId);
}
