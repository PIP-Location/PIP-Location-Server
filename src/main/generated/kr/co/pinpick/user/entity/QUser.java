package kr.co.pinpick.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1348614246L;

    public static final QUser user = new QUser("user");

    public final kr.co.pinpick.common.QBaseEntity _super = new kr.co.pinpick.common.QBaseEntity(this);

    public final ListPath<kr.co.pinpick.archive.entity.Archive, kr.co.pinpick.archive.entity.QArchive> archives = this.<kr.co.pinpick.archive.entity.Archive, kr.co.pinpick.archive.entity.QArchive>createList("archives", kr.co.pinpick.archive.entity.Archive.class, kr.co.pinpick.archive.entity.QArchive.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final StringPath email = createString("email");

    public final ListPath<Follower, QFollower> followers = this.<Follower, QFollower>createList("followers", Follower.class, QFollower.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath profileImage = createString("profileImage");

    public final EnumPath<kr.co.pinpick.user.entity.enumerated.RoleType> role = createEnum("role", kr.co.pinpick.user.entity.enumerated.RoleType.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

