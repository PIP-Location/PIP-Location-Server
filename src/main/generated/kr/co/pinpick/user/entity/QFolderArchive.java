package kr.co.pinpick.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFolderArchive is a Querydsl query type for FolderArchive
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFolderArchive extends EntityPathBase<FolderArchive> {

    private static final long serialVersionUID = -108014023L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFolderArchive folderArchive = new QFolderArchive("folderArchive");

    public final kr.co.pinpick.common.QBaseEntity _super = new kr.co.pinpick.common.QBaseEntity(this);

    public final kr.co.pinpick.archive.entity.QArchive archive;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QFolder folder;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public QFolderArchive(String variable) {
        this(FolderArchive.class, forVariable(variable), INITS);
    }

    public QFolderArchive(Path<? extends FolderArchive> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFolderArchive(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFolderArchive(PathMetadata metadata, PathInits inits) {
        this(FolderArchive.class, metadata, inits);
    }

    public QFolderArchive(Class<? extends FolderArchive> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.archive = inits.isInitialized("archive") ? new kr.co.pinpick.archive.entity.QArchive(forProperty("archive"), inits.get("archive")) : null;
        this.folder = inits.isInitialized("folder") ? new QFolder(forProperty("folder"), inits.get("folder")) : null;
    }

}

