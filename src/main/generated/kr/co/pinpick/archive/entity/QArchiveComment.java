package kr.co.pinpick.archive.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArchiveComment is a Querydsl query type for ArchiveComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArchiveComment extends EntityPathBase<ArchiveComment> {

    private static final long serialVersionUID = 2079060105L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArchiveComment archiveComment = new QArchiveComment("archiveComment");

    public final kr.co.pinpick.common.QBaseEntity _super = new kr.co.pinpick.common.QBaseEntity(this);

    public final QArchive archive;

    public final kr.co.pinpick.user.entity.QUser author;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QArchiveComment parent;

    public final ListPath<ArchiveComment, QArchiveComment> subComments = this.<ArchiveComment, QArchiveComment>createList("subComments", ArchiveComment.class, QArchiveComment.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QArchiveComment(String variable) {
        this(ArchiveComment.class, forVariable(variable), INITS);
    }

    public QArchiveComment(Path<? extends ArchiveComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArchiveComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArchiveComment(PathMetadata metadata, PathInits inits) {
        this(ArchiveComment.class, metadata, inits);
    }

    public QArchiveComment(Class<? extends ArchiveComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.archive = inits.isInitialized("archive") ? new QArchive(forProperty("archive"), inits.get("archive")) : null;
        this.author = inits.isInitialized("author") ? new kr.co.pinpick.user.entity.QUser(forProperty("author")) : null;
        this.parent = inits.isInitialized("parent") ? new QArchiveComment(forProperty("parent"), inits.get("parent")) : null;
    }

}

