package kr.co.pinpick.common.service;

import kr.co.pinpick.user.entity.User;

public interface IUserLinkService<T> {
    void link(User source, T target);
    int unlink(User source, T target);
}
