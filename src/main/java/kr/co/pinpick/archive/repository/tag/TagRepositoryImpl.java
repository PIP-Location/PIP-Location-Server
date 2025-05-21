package kr.co.pinpick.archive.repository.tag;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pinpick.archive.entity.Tag;
import kr.co.pinpick.search.dto.request.SearchRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static kr.co.pinpick.archive.entity.QTag.tag;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Tag> search(SearchRequest request) {
        return queryFactory
                .selectFrom(tag)
                .where(containingQ(request.getQ()))
                .orderBy(tag.count.desc())
                .limit(request.getLimit())
                .fetch();
    }

    private BooleanExpression containingQ(String q) {
        return hasText(q) ? tag.name.contains(q) : null;
    }
}
