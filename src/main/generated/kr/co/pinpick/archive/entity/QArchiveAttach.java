package kr.co.pinpick.archive.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArchiveAttach is a Querydsl query type for ArchiveAttach
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArchiveAttach extends EntityPathBase<ArchiveAttach> {

    private static final long serialVersionUID = -262471973L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArchiveAttach archiveAttach = new QArchiveAttach("archiveAttach");

    public final QArchive archive;

    public final NumberPath<Integer> height = createNumber("height", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath path = createString("path");

    public final NumberPath<Byte> sequence = createNumber("sequence", Byte.class);

    public final NumberPath<Integer> width = createNumber("width", Integer.class);

    public QArchiveAttach(String variable) {
        this(ArchiveAttach.class, forVariable(variable), INITS);
    }

    public QArchiveAttach(Path<? extends ArchiveAttach> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArchiveAttach(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArchiveAttach(PathMetadata metadata, PathInits inits) {
        this(ArchiveAttach.class, metadata, inits);
    }

    public QArchiveAttach(Class<? extends ArchiveAttach> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.archive = inits.isInitialized("archive") ? new QArchive(forProperty("archive"), inits.get("archive")) : null;
    }

}

