package kr.co.pinpick.archive.repository.archive;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pinpick.archive.dto.response.ArchiveRetrieveRequest;
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
        var query = queryFactory.select(archive).from(archive)
                .where(
                        ltArchiveId(request.getArchiveId()),
                        JPAExpressions.selectFrom(block1)
                                .where(block1.author.eq(user))
                                .where(block1.block.eq(archive.author))
                                .notExists()
                );

        // 위치 검색
        if (
                request.getTopLeftLatitude() != null &&
                        request.getTopLeftLongitude() != null &&
                        request.getBottomRightLatitude() != null &&
                        request.getBottomRightLongitude() != null
        ) {
            String polygonWKT = String.format("ST_POLYGONFROMTEXT('POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))')",
                    request.getBottomRightLongitude(), request.getBottomRightLatitude(),   // southWest
                    request.getBottomRightLongitude(), request.getTopLeftLatitude(),   // northWest
                    request.getTopLeftLongitude(), request.getTopLeftLatitude(),   // northEast
                    request.getTopLeftLongitude(), request.getBottomRightLatitude(),   // southEast
                    request.getBottomRightLongitude(), request.getBottomRightLatitude());

            query.where(
                    Expressions.booleanTemplate(
                            "MBRContains(" + polygonWKT + ", {0})",
                            archive.location
                    ).isTrue()
            );
        }

        // 팔로우
        if (request.getFollow() != null && user != null) {
            var subQuery = JPAExpressions.selectFrom(follower1)
                    .where(follower1.follower.eq(user))
                    .where(follower1.follow.eq(archive.author));

            query.where(request.getFollow() ? subQuery.exists() : subQuery.notExists());
        }

        // 태그 검색
        if (request.getTag() != null) {
            var subQuery = JPAExpressions.selectFrom(archiveTag)
                    .where(archiveTag.archive.eq(archive))
                    .where(archiveTag.name.eq(request.getTag()));

            query.where(subQuery.exists());
        }

        return query.orderBy(archive.createdAt.desc()).fetch();
    }

    private BooleanExpression ltArchiveId(Long archiveId) {
        if (archiveId == null) {
            return null;
        }

        return archive.id.lt(archiveId);
    }
}
