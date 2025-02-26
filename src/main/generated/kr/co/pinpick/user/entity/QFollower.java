package kr.co.pinpick.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFollower is a Querydsl query type for Follower
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFollower extends EntityPathBase<Follower> {

    private static final long serialVersionUID = -1943361735L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFollower follower1 = new QFollower("follower1");

    public final kr.co.pinpick.common.QBaseEntity _super = new kr.co.pinpick.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QUser follow;

    public final QUser follower;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public QFollower(String variable) {
        this(Follower.class, forVariable(variable), INITS);
    }

    public QFollower(Path<? extends Follower> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFollower(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFollower(PathMetadata metadata, PathInits inits) {
        this(Follower.class, metadata, inits);
    }

    public QFollower(Class<? extends Follower> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.follow = inits.isInitialized("follow") ? new QUser(forProperty("follow")) : null;
        this.follower = inits.isInitialized("follower") ? new QUser(forProperty("follower")) : null;
    }

}

