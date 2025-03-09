package kr.co.pinpick.user.service.relationship;


import kr.co.pinpick.user.entity.User;

public interface IUserRelationshipService {
    void link(User source, User target);
    int unlink(User source, User target);
}
