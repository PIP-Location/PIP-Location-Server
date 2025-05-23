package kr.co.pinpick.archive.repository.archiveComment;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.entity.ArchiveComment;
import kr.co.pinpick.common.dto.request.NoOffsetPaginateRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static kr.co.pinpick.archive.entity.QArchiveComment.archiveComment;

@RequiredArgsConstructor
public class ArchiveCommentRepositoryImpl implements ArchiveCommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ArchiveComment> findByArchiveAndParentIsNull(Archive archive, NoOffsetPaginateRequest request) {
        return queryFactory
                .selectFrom(archiveComment)
                .where(
                        archiveComment.archive.eq(archive),
                        archiveComment.parent.isNull(),
                        ltArchiveCommentId(request.getLastId()),
                        archiveComment.isDeleted.isFalse()
                )
                .limit(request.getLimit())
                .fetch();
    }

    /** 페이지네이션 */
    private BooleanExpression ltArchiveCommentId(Long lastId) {
        return lastId == null ? null : archiveComment.id.lt(lastId);
    }
}
