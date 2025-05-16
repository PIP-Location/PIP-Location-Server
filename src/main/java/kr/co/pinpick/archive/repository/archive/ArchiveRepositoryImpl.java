package kr.co.pinpick.archive.repository.archive;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pinpick.archive.dto.request.ArchiveRetrieveRequest;
import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.entity.QArchive;
import kr.co.pinpick.common.dto.request.OffsetPaginateRequest;
import kr.co.pinpick.common.dto.request.SearchRequest;
import kr.co.pinpick.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static kr.co.pinpick.archive.entity.QArchive.archive;
import static kr.co.pinpick.archive.entity.QArchiveTag.archiveTag;
import static kr.co.pinpick.user.entity.QBlock.block1;
import static kr.co.pinpick.user.entity.QFollower.follower1;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class ArchiveRepositoryImpl implements ArchiveRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Archive> retrieve(User user, ArchiveRetrieveRequest request) {
        var query = queryFactory.selectFrom(archive)
                .where(
                        locationInBounds(request),
                        followFilter(user, request),
                        blockFilter(user),
                        userFilter(request),
                        tagFilter(request),
                        visibilityFilter(user),
                        ltArchiveId(request.getLastId()),
                        archive.isDeleted.isFalse()
                )
                .orderBy(archive.createdAt.desc());

        if (!isRetrieveFromMap(request)) {
            query.limit(request.getLimit());
        }

        return query.fetch();
    }

    /** 위치 필터 */
    private BooleanExpression locationInBounds(ArchiveRetrieveRequest request) {
        if (!isRetrieveFromMap(request)) {
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
        if (user == null || request.getFollow() == null || !isRetrieveFromMap(request)) return null;

        var subQuery = JPAExpressions.selectFrom(follower1)
                .where(follower1.follower.eq(user))
                .where(follower1.follow.eq(archive.user));

        return request.getFollow() ? subQuery.exists() : subQuery.notExists();
    }

    /** 차단된 유저의 아카이브 제외 */
    private BooleanExpression blockFilter(User user) {
        if (user == null) return null;
        return JPAExpressions.selectFrom(block1)
                .where(block1.user.eq(user))
                .where(block1.block.eq(archive.user))
                .notExists();
    }

    /** 유저 필터 */
    private BooleanExpression userFilter(ArchiveRetrieveRequest request) {
        return request.getUserId() == null ? null : archive.user.id.eq(request.getUserId());
    }

    /** 태그 필터 */
    private BooleanExpression tagFilter(ArchiveRetrieveRequest request) {
        if (request.getTag() == null) return null;
        return JPAExpressions.selectFrom(archiveTag)
                .where(archiveTag.archive.eq(archive))
                .where(archiveTag.name.eq(request.getTag()))
                .notExists();
    }

    /** 공개 여부 필터 */
    private BooleanExpression visibilityFilter(User user) {
        return archive.user.eq(user).or(archive.isPublic.isTrue());
    }

    /** 페이지네이션 */
    private BooleanExpression ltArchiveId(Long lastId) {
        return lastId == null ? null : archive.id.lt(lastId);
    }

    private boolean isRetrieveFromMap(ArchiveRetrieveRequest request) {
        return request.getTopLeftLatitude() != null &&
                request.getTopLeftLongitude() != null &&
                request.getBottomRightLatitude() != null &&
                request.getBottomRightLongitude() != null;
    }

    @Override
    public List<Archive> findAllByUser(User principal, User user) {
        return queryFactory
                .selectFrom(archive)
                .where(
                        archive.user.eq(user),
                        isMe(principal, user),
                        archive.isDeleted.isFalse()
                )
                .fetch();
    }

    private BooleanExpression isMe(User principal, User user) {
        return principal.getId().equals(user.getId()) ? null : archive.isPublic.isTrue();
    }

    @Override
    public List<Archive> search(SearchRequest request) {
        return queryFactory
                .selectFrom(archive)
                .where(
                        containingQ(request.getQ()),
                        archive.isDeleted.isFalse()
                )
                .orderBy(archive.name.asc())
                .limit(request.getLimit())
                .fetch();
    }

    private BooleanExpression containingQ(String q) {
        return hasText(q) ? archive.name.contains(q) : null;
    }

    @Override
    public List<User> findRepipByArchive(Archive source, OffsetPaginateRequest request) {
        QArchive archive = QArchive.archive;
        QArchive original = new QArchive("original");

        return queryFactory
                .select(archive.user)
                .from(archive)
                .join(archive.repipArchive, original)
                .where(
                        archive.repipArchive.eq(source),
                        archive.isDeleted.isFalse()
                )
                .offset(request.getPage() * request.getLimit())
                .limit(request.getLimit())
                .orderBy(archive.createdAt.desc())
                .fetch();
    }
}
