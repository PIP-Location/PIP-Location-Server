package kr.co.pinpick.archive.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArchiveReaction is a Querydsl query type for ArchiveReaction
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArchiveReaction extends EntityPathBase<ArchiveReaction> {

    private static final long serialVersionUID = -238740161L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArchiveReaction archiveReaction = new QArchiveReaction("archiveReaction");

    public final kr.co.pinpick.common.QBaseEntity _super = new kr.co.pinpick.common.QBaseEntity(this);

    public final QArchive archive;

    public final kr.co.pinpick.user.entity.QUser author;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final EnumPath<kr.co.pinpick.archive.entity.enumerated.ReactionType> reactionType = createEnum("reactionType", kr.co.pinpick.archive.entity.enumerated.ReactionType.class);

    public QArchiveReaction(String variable) {
        this(ArchiveReaction.class, forVariable(variable), INITS);
    }

    public QArchiveReaction(Path<? extends ArchiveReaction> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArchiveReaction(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArchiveReaction(PathMetadata metadata, PathInits inits) {
        this(ArchiveReaction.class, metadata, inits);
    }

    public QArchiveReaction(Class<? extends ArchiveReaction> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.archive = inits.isInitialized("archive") ? new QArchive(forProperty("archive"), inits.get("archive")) : null;
        this.author = inits.isInitialized("author") ? new kr.co.pinpick.user.entity.QUser(forProperty("author")) : null;
    }

}

