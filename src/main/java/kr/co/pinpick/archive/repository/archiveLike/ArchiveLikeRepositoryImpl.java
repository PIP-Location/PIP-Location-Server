package kr.co.pinpick.archive.repository.archiveLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.common.dto.request.OffsetPaginateRequest;
import kr.co.pinpick.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static kr.co.pinpick.archive.entity.QArchive.archive;
import static kr.co.pinpick.archive.entity.QArchiveLike.archiveLike;

@RequiredArgsConstructor
public class ArchiveLikeRepositoryImpl implements ArchiveLikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<User> findLikeByArchive(Archive source, OffsetPaginateRequest request) {
        return queryFactory
                .select(archiveLike.user)
                .from(archiveLike)
                .join(archiveLike.archive, archive)
                .where(archiveLike.archive.eq(source))
                .offset(request.getPage() * request.getLimit())
                .limit(request.getLimit())
                .orderBy(archiveLike.createdAt.desc())
                .fetch();
    }
}
