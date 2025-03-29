package kr.co.pinpick.archive.repository.archive;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pinpick.archive.dto.request.ArchiveRetrieveRequest;
import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static kr.co.pinpick.archive.entity.QArchive.archive;
import static kr.co.pinpick.archive.entity.QArchiveTag.archiveTag;
import static kr.co.pinpick.user.entity.QBlock.block1;
import static kr.co.pinpick.user.entity.QFollower.follower1;

@RequiredArgsConstructor
public class ArchiveRepositoryImpl implements ArchiveRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Archive> retrieve(User user, ArchiveRetrieveRequest request) {
        return queryFactory.selectFrom(archive)
                .where(
                        ltArchiveId(request.getArchiveId()),
                        notBlockedByUser(user),
                        locationInBounds(request),
                        followFilter(user, request),
                        hasTag(request.getTag())
                )
                .orderBy(archive.createdAt.desc())
                .limit(20)
                .fetch();
    }

    /** 차단된 유저의 아카이브 제외 */
    private BooleanExpression notBlockedByUser(User user) {
        if (user == null) return null;
        return JPAExpressions.selectFrom(block1)
                .where(block1.author.eq(user))
                .where(block1.block.eq(archive.author))
                .notExists();
    }

    /** 위치 필터 */
    private BooleanExpression locationInBounds(ArchiveRetrieveRequest request) {
        if (request.getTopLeftLatitude() == null ||
                request.getTopLeftLongitude() == null ||
                request.getBottomRightLatitude() == null ||
                request.getBottomRightLongitude() == null) {
            return null;
        }

        String polygonWKT = String.format(
                "ST_POLYGONFROMTEXT('POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))')",
                request.getBottomRightLongitude(), request.getBottomRightLatitude(),   // southWest
                request.getBottomRightLongitude(), request.getTopLeftLatitude(),       // northWest
                request.getTopLeftLongitude(), request.getTopLeftLatitude(),           // northEast
                request.getTopLeftLongitude(), request.getBottomRightLatitude(),       // southEast
                request.getBottomRightLongitude(), request.getBottomRightLatitude()
        );

        return Expressions.booleanTemplate("MBRContains(" + polygonWKT + ", {0})", archive.location).isTrue();
    }

    /** 팔로우 필터 */
    private BooleanExpression followFilter(User user, ArchiveRetrieveRequest request) {
        if (user == null || request.getFollow() == null) return null;

        var subQuery = JPAExpressions.selectFrom(follower1)
                .where(follower1.follower.eq(user))
                .where(follower1.follow.eq(archive.author));

        return request.getFollow() ? subQuery.exists() : subQuery.notExists();
    }

    /** 태그 필터 */
    private BooleanExpression hasTag(String tag) {
        if (tag == null) return null;

        return JPAExpressions.selectFrom(archiveTag)
                .where(archiveTag.archive.eq(archive))
                .where(archiveTag.name.eq(tag))
                .exists();
    }

    /** 페이지네이션 */
    private BooleanExpression ltArchiveId(Long archiveId) {
        return archiveId == null ? null : archive.id.lt(archiveId);
    }

    @Override
    public List<Archive> findAllByAuthor(User user, User author) {
        return queryFactory
                .selectFrom(archive)
                .where(archive.author.eq(author), isMe(user.getId(), author.getId()))
                .fetch();
    }

    private BooleanExpression isMe(Long userId, Long authorId) {
        return userId.equals(authorId) ? null : archive.isPublic.isTrue();
    }
}
