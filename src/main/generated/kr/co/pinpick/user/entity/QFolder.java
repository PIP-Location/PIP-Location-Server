package kr.co.pinpick.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFolder is a Querydsl query type for Folder
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFolder extends EntityPathBase<Folder> {

    private static final long serialVersionUID = -1494766007L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFolder folder = new QFolder("folder");

    public final kr.co.pinpick.common.QBaseEntity _super = new kr.co.pinpick.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<FolderArchive, QFolderArchive> folderArchives = this.<FolderArchive, QFolderArchive>createList("folderArchives", FolderArchive.class, QFolderArchive.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final BooleanPath isPublic = createBoolean("isPublic");

    public final StringPath name = createString("name");

    public final QUser user;

    public QFolder(String variable) {
        this(Folder.class, forVariable(variable), INITS);
    }

    public QFolder(Path<? extends Folder> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFolder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFolder(PathMetadata metadata, PathInits inits) {
        this(Folder.class, metadata, inits);
    }

    public QFolder(Class<? extends Folder> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

