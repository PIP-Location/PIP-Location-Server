package kr.co.pinpick.archive.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArchiveTag is a Querydsl query type for ArchiveTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArchiveTag extends EntityPathBase<ArchiveTag> {

    private static final long serialVersionUID = 136537796L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArchiveTag archiveTag = new QArchiveTag("archiveTag");

    public final kr.co.pinpick.common.QBaseEntity _super = new kr.co.pinpick.common.QBaseEntity(this);

    public final QArchive archive;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> sequence = createNumber("sequence", Integer.class);

    public QArchiveTag(String variable) {
        this(ArchiveTag.class, forVariable(variable), INITS);
    }

    public QArchiveTag(Path<? extends ArchiveTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArchiveTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArchiveTag(PathMetadata metadata, PathInits inits) {
        this(ArchiveTag.class, metadata, inits);
    }

    public QArchiveTag(Class<? extends ArchiveTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.archive = inits.isInitialized("archive") ? new QArchive(forProperty("archive"), inits.get("archive")) : null;
    }

}

