package kr.co.pinpick.archive.repository.tag;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pinpick.archive.dto.request.TagRetrieveRequest;
import kr.co.pinpick.archive.entity.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static kr.co.pinpick.archive.entity.QTag.tag;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Tag> findAllByName(TagRetrieveRequest request) {
        return queryFactory
                .selectFrom(tag)
                .where(containingQ(request.getQ()))
                .orderBy(tag.count.desc())
                .limit(20)
                .offset(request.getPageNo() * 20L)
                .fetch();
    }

    private BooleanExpression containingQ(String q) {
        return hasText(q) ? tag.name.contains(q) : null;
    }
}
