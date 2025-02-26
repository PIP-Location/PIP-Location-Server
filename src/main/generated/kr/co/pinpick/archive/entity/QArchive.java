package kr.co.pinpick.archive.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArchive is a Querydsl query type for Archive
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArchive extends EntityPathBase<Archive> {

    private static final long serialVersionUID = -1571736330L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArchive archive = new QArchive("archive");

    public final kr.co.pinpick.common.QBaseEntity _super = new kr.co.pinpick.common.QBaseEntity(this);

    public final StringPath address = createString("address");

    public final ListPath<ArchiveAttach, QArchiveAttach> archiveAttaches = this.<ArchiveAttach, QArchiveAttach>createList("archiveAttaches", ArchiveAttach.class, QArchiveAttach.class, PathInits.DIRECT2);

    public final SetPath<ArchiveComment, QArchiveComment> archiveComments = this.<ArchiveComment, QArchiveComment>createSet("archiveComments", ArchiveComment.class, QArchiveComment.class, PathInits.DIRECT2);

    public final SetPath<ArchiveReaction, QArchiveReaction> archiveReactions = this.<ArchiveReaction, QArchiveReaction>createSet("archiveReactions", ArchiveReaction.class, QArchiveReaction.class, PathInits.DIRECT2);

    public final kr.co.pinpick.user.entity.QUser author;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<kr.co.pinpick.user.entity.FolderArchive, kr.co.pinpick.user.entity.QFolderArchive> folderArchives = this.<kr.co.pinpick.user.entity.FolderArchive, kr.co.pinpick.user.entity.QFolderArchive>createList("folderArchives", kr.co.pinpick.user.entity.FolderArchive.class, kr.co.pinpick.user.entity.QFolderArchive.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final BooleanPath isPublic = createBoolean("isPublic");

    public final SimplePath<org.springframework.data.geo.Point> location = createSimple("location", org.springframework.data.geo.Point.class);

    public final StringPath name = createString("name");

    public final NumberPath<Double> positionX = createNumber("positionX", Double.class);

    public final NumberPath<Double> positionY = createNumber("positionY", Double.class);

    public final ListPath<ArchiveTag, QArchiveTag> tags = this.<ArchiveTag, QArchiveTag>createList("tags", ArchiveTag.class, QArchiveTag.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QArchive(String variable) {
        this(Archive.class, forVariable(variable), INITS);
    }

    public QArchive(Path<? extends Archive> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArchive(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArchive(PathMetadata metadata, PathInits inits) {
        this(Archive.class, metadata, inits);
    }

    public QArchive(Class<? extends Archive> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new kr.co.pinpick.user.entity.QUser(forProperty("author")) : null;
    }

}

